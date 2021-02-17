package ai.infrrd.idc.receipt.fieldextractor.merchantname.util.preprocessor;

import java.util.*;

public final class LineItemUtils {
    private static final String NUMBERS_REGEX = ".*([\\d]+([,.][\\d]{0,2})\\s?.?)";
    private static final String MULTIPLIER_FINDER_REGEX = ".*\\d+\\s*[Xx*].*";

    // TODO: Read these from DB or elsewhere
    private static final List<String> LI_STOP_WORDS_TOTAL = Arrays.asList( "payment", "balance due", "sale amount",
            "amount due", "amt", "paid", "fare", "pay", "amount", "visa", "grand total", "card", "master card", "mcard",
            "visa card", "american express", "gr. tot", "gr.tot", "etendercredit", "amex", "change", "total due in us dollars  ",
            "grand total", "total for this order", "invoice total", "shipment total", "total amt", "total incl", "total", "betrag",
            "gesamt", "summe", "netto gesamt", "gesamtbetrag", "totaal", "te betalen", "netto", "tot", "kosten", "total (incl vat)",
            "sub-totaal", "total charges", "totaal bedrag", "total ttc", "bancontact", "srvc tl" );
    private static final List<String> LI_STOP_WORDS_VAT = Arrays.asList( "mwst", "vat", "btw" );


    private LineItemUtils()
    {}


    public static double getScoreForLineItem( int idx, int lineCount, String origLine )
    {
        String line = origLine.trim();
        double result = 0.0;
        result = adjustForNumbers( result, line );
        result = adjustForPosition( result, idx, lineCount );
        result = adjustForMultipliersInBeg( result, line );
        return result;

    }


    private static double adjustForMultipliersInBeg( double oldResult, String line )
    {
        double result = oldResult;
        if ( line.matches( MULTIPLIER_FINDER_REGEX ) ) {
            result += SCORE.MULTIPLIER_FOUND.getValue();
        }
        return result;
    }


    private static double adjustForPosition( double oldResult, int idx, int lineCount )
    {
        int mid = lineCount / 2;
        int distance = Math.abs( idx - mid );
        double factor = 1 - ( (double) distance / mid );
        double score = factor * SCORE.LINE_NEAR_MIDDLE.getValue();
        return score + oldResult;
    }


    private static Double adjustForNumbers( double oldResult, String line )
    {
        double result = oldResult;
        if ( line.matches( NUMBERS_REGEX ) ) {
            result += SCORE.LAST_TOKEN_NUM.getValue();
        }
        return result;
    }


    public static boolean containsStopWord( String line )
    {
        for ( String stopWord : LI_STOP_WORDS_TOTAL ) {
            if ( line.toLowerCase().contains( stopWord ) ) {
                return true;
            }
        }

        for ( String stopWord : LI_STOP_WORDS_VAT ) {
            if ( line.toLowerCase().contains( stopWord ) ) {
                return true;
            }
        }
        return false;
    }


    public enum SCORE
    {
        LAST_TOKEN_NUM( 0.8 ),
        SECOND_LAST_TOKEN_NUM( 0.6 ),
        LINE_NEAR_MIDDLE( 0.3 ),
        MULTIPLIER_FOUND( 0.8 ),
        THRESHOLD( 0.85 );
        double value;


        SCORE( double val )
        {
            this.value = val;
        }


        public double getValue()
        {
            return this.value;
        }
    }


    public static List<LineItemResult> convergeResults( List<String> lines, List<LineItemResult> results )
    {
        if ( lines == null || results == null ) {
            throw new IllegalArgumentException( "Parameters <lines> and <results> can't be null" );
        }
        List<LineItemResult> finalResults = new ArrayList<>();
        Set<Integer> indexes = new TreeSet<>();
        for ( LineItemResult result : results ) {
            for ( int i = result.getStartIdx(); i <= result.getEndIdx(); i++ ) {
                indexes.add( i );
            }
        }

        int prevIdx = -1;
        LineItemResult intermediateResult = new LineItemResult();

        for ( int idx : indexes ) {
            if ( prevIdx == -1 ) {
                intermediateResult.setStartIdx( idx );
            } else if ( idx != ( prevIdx + 1 ) ) {
                intermediateResult.setEndIdx( prevIdx );
                List<String> theLines = getLines( lines, intermediateResult );
                intermediateResult.setTheLines( theLines );
                finalResults.add( intermediateResult );
                intermediateResult = new LineItemResult();
                intermediateResult.setStartIdx( idx );
            }
            prevIdx = idx;
        }
        if ( intermediateResult.getStartIdx() != null && prevIdx != -1 ) {
            intermediateResult.setEndIdx( prevIdx );
            List<String> theLines = getLines( lines, intermediateResult );
            intermediateResult.setTheLines( theLines );
            finalResults.add( intermediateResult );
        }

        return finalResults;
    }


    private static List<String> getLines( List<String> lines, LineItemResult intermediateResult )
    {
        List<String> theLines = new ArrayList<>();
        for ( int j = intermediateResult.getStartIdx(); j <= intermediateResult.getEndIdx(); j++ ) {
            if ( j < lines.size() ) {
                theLines.add( lines.get( j ) );
            }
        }
        return theLines;
    }
}
