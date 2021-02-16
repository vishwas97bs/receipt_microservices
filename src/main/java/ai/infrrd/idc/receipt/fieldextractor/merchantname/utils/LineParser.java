package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

import java.util.Map;

public interface LineParser {
    /**
     * Converts a String to a Map<String, Object>.
     * <br> Return null if the conversion failed
     * @param string string to be parsed
     * @return parsed String in map
     */
    Map<String, Object> parse(String string );
}
