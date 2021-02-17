package ai.infrrd.idc.receipt.fieldextractor.merchantname.util.preprocessor;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FindTablesRule extends Rule {
    private static final int MAX_TOKENS = 6;
    private static final int MIN_TOKENS = 2;


    @Override
    public List<LineItemResult> apply(List<String> lines )
    {
        // Count number of tokens in each line
        int i = 0;
        int prevTokenCount = -1;
        int currentConsecutiveCount = 0;
        int highestConsecutiveCount = 0;
        int endIdx = 0;
        for ( String line : lines ) {
            if ( !StringUtils.isEmpty( line ) ) {
                if ( LineItemUtils.containsStopWord( line ) ) {
                    break;
                }
                int tokenCount = line.trim().split( "\\s{2,}" ).length;

                if ( tokenCount == prevTokenCount && toConsider( tokenCount ) ) {
                    // If this line has same number of tokens as previous line
                    currentConsecutiveCount++;
                    if ( currentConsecutiveCount > highestConsecutiveCount ) {
                        // If this count exceeds the previous count of highest consecutive occurrences
                        endIdx = i;
                        highestConsecutiveCount = currentConsecutiveCount;
                    }
                } else {
                    // Reset
                    currentConsecutiveCount = 0;
                }
                prevTokenCount = tokenCount;
            }
            i++;
        }


        return getResult( lines, highestConsecutiveCount, endIdx );
    }


    private List<LineItemResult> getResult( List<String> lines, int highestConsecutiveCount, int endIdx )
    {
        if ( highestConsecutiveCount > 0 ) {
            double totalScore = 0.0;
            List<String> theLines = new ArrayList<>();
            int startIdx = endIdx - highestConsecutiveCount;
            for ( int j = startIdx; j <= endIdx; j++ ) {
                theLines.add( lines.get( j ).trim() );
                totalScore += LineItemUtils.getScoreForLineItem( j, lines.size(), lines.get( j ) );
            }

            if ( ( totalScore / ( highestConsecutiveCount + 1 ) ) > LineItemUtils.SCORE.THRESHOLD.getValue() ) {

                LineItemResult res = new LineItemResult();
                res.setOperation( this.getClass().getSimpleName() );
                res.setTheLines( theLines );
                res.setStartIdx( startIdx );
                res.setEndIdx( endIdx );
                return Collections.singletonList( res );
            } else {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }


    private boolean toConsider( int tokenCount )
    {
        return tokenCount >= MIN_TOKENS && tokenCount <= MAX_TOKENS;
    }

}
