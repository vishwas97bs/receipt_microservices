package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

/**
 * Entity Class for NGram
 * @author ritesh
 * @since 1/6/17.
 */
public class NGram
{
    private String matchedVal;
    private String nGramStr;
    private Integer nGramSize;
    private Double line;
    private Double score;
    private int lineIndex;


    public String getMatchedVal()
    {
        return matchedVal;
    }


    public void setMatchedVal( String matchedVal )
    {
        this.matchedVal = matchedVal;
    }


    public String getNGramStr()
    {
        return nGramStr;
    }


    public void setNGramStr( String nGramStr )
    {
        this.nGramStr = nGramStr;
    }


    public Integer getNGramSize()
    {
        return nGramSize;
    }


    public void setNGramSize( Integer nGramSize )
    {
        this.nGramSize = nGramSize;
    }


    public Double getLine()
    {
        return line;
    }


    public void setLine( Double line )
    {
        this.line = line;
    }


    public Double getScore()
    {
        return score;
    }


    public void setScore( Double score )
    {
        this.score = score;
    }


    public NGram( String nGramStr, Integer nGramSize, Double line, Double score )
    {
        this.nGramStr = nGramStr;
        this.nGramSize = nGramSize;
        this.line = line;
        this.score = score;
    }


    @Override
    public String toString()
    {
        return nGramStr + " " + "[nGramSize=" + nGramSize + ", line=" + line + ", score=" + score + "]";
    }


    public int getLineIndex()
    {
        return lineIndex;
    }


    public void setLineIndex( int lineIndex )
    {
        this.lineIndex = lineIndex;
    }

}
