package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

import java.util.Comparator;


/**
 * Comparator for sorting NGram
 * @author ritesh
 * @since 2/6/17.
 */
public class NGramComparator implements Comparator<NGram>
{
    @Override
    public int compare( NGram nGram1, NGram nGram2 )
    {
        int line = nGram1.getLine().compareTo( nGram2.getLine() );
        int nGramSize = nGram1.getNGramSize().compareTo( nGram2.getNGramSize() );
        int score = nGram1.getScore().compareTo( nGram2.getScore() );

        if ( line == 0 ) {
            return ( nGramSize == 0 ) ? score : nGramSize;
        } else {
            return line;
        }
    }
}