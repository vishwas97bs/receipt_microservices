package ai.infrrd.idc.receipt.fieldextractor.merchantname.util.preprocessor;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class FindMaxCharsRule extends Rule {
    private static final double LI_THRESHOLD_LENGTH_FRACTION = 0.75;
    private static final int MISMATCHES_THRESHOLD = 0;


    @Override
    public List<LineItemResult> apply(List<String> lines )
    {
        Map<Integer, Integer> occCount = new HashMap<>();
        int startIdx = 0;

        int avgCharCount = getAvgCharCount( lines );

        boolean alreadyRunning = false;
        int i = 0;
        int numMismatches = 0;
        int currLineCount = 0;
        for ( String line : lines ) {
            if ( LineItemUtils.containsStopWord( line ) ) {
                occCount.put( startIdx, currLineCount );
                break;
            }
            int length = line.trim().length();
            if ( length > ( LI_THRESHOLD_LENGTH_FRACTION * avgCharCount ) ) {
                currLineCount++;
                if ( !alreadyRunning ) {
                    // This is where we start considering
                    alreadyRunning = true;
                    startIdx = i;
                }
            } else {
                numMismatches++;
                if ( alreadyRunning ) {
                    if ( toConsider( numMismatches ) ) {
                        currLineCount++;
                        occCount.put( startIdx, currLineCount );
                    } else {
                        numMismatches = 0;
                        alreadyRunning = false;
                        occCount.put( startIdx, currLineCount );
                        currLineCount = 0;
                    }
                } else {
                    numMismatches = 0;
                    currLineCount = 0;
                }
            }
            i++;
        }

        return getResult( lines, startIdx, occCount );
    }


    private int getAvgCharCount( List<String> lines )
    {
        int avgCharCount = 0;
        int realLineCount = 0;
        for ( String line : lines ) {
            if ( !StringUtils.isEmpty( line ) ) {
                int length = line.length();
                avgCharCount += length;
                realLineCount++;
            }
        }

        if ( realLineCount > 0 ) {
            avgCharCount /= realLineCount;
        }
        return avgCharCount;
    }


    private List<LineItemResult> getResult( List<String> lines, int start, Map<Integer, Integer> occCount )
    {
        int endIdx;
        int startIdx = start;
        while ( !occCount.isEmpty() ) {
            int max = 0;
            for ( Map.Entry<Integer, Integer> entry : occCount.entrySet() ) {
                if ( entry.getValue() > max ) {
                    startIdx = entry.getKey();
                    max = entry.getValue();
                }
            }
            if ( ( occCount.remove( startIdx ) == null ) || max <= 0 ) {
                break;
            }
            endIdx = startIdx + max;
            double totalScore = 0.0;
            List<String> theLines = new ArrayList<>();
            for ( int j = startIdx; j < endIdx; j++ ) {
                String theLine = lines.get( j ).trim();
                theLines.add( theLine );
                double scoreForLineItem = LineItemUtils.getScoreForLineItem( j, lines.size(), theLine );
                totalScore += scoreForLineItem;
            }

            if ( ( totalScore / ( max ) ) > LineItemUtils.SCORE.THRESHOLD.getValue() ) {

                LineItemResult res = new LineItemResult();
                res.setOperation( this.getClass().getSimpleName() );
                res.setTheLines( theLines );
                res.setStartIdx( startIdx );
                res.setEndIdx( endIdx - 1 );
                return Collections.singletonList( res );
            }
        }

        return Collections.emptyList();

    }


    private boolean toConsider( int numMismatches )
    {
        return numMismatches <= MISMATCHES_THRESHOLD;
    }

}
