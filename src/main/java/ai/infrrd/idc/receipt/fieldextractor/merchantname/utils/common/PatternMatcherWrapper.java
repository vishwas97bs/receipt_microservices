package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;

public class PatternMatcherWrapper
{
    PatternExtractor patternExtractor;
    String inputString;


    public PatternMatcherWrapper( PatternExtractor patternExtractor, String inputString )
    {
        this.patternExtractor = patternExtractor;
        this.inputString = inputString;
    }


    public PatternExtractor getPatternExtractor()
    {
        return patternExtractor;
    }


    public void setPatternExtractor( PatternExtractor patternExtractor )
    {
        this.patternExtractor = patternExtractor;
    }


    public String getInputString()
    {
        return inputString;
    }


    public void setInputString( String inputString )
    {
        this.inputString = inputString;
    }
}
