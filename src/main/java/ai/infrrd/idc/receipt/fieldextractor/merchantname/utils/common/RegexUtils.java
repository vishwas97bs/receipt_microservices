package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;

public class RegexUtils
{
    /**
     * Checks if the given string has words matching the specified regex pattern
     *
     * @param input Input string to check for regex pattern
     * @param regex The regex pattern to be checked
     * @return true is there is a regex pattern match else false
     */
    public static boolean checkIfStringContainsRegexPattern(String input, String regex )
    {
        PatternExtractor patternExtractor = new PatternExtractor( regex );
        return patternExtractor.isMatchedPatterns( input );
    }
}
