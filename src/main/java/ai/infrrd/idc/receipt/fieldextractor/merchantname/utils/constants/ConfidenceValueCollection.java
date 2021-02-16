package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants;

public class ConfidenceValueCollection {
    public static final double EXISTING_MERCHANT_LIST_EXTRACTOR = 0.8;
    public static final double GOOGLE_VISION_MERCHANT_EXTRACTOR = 0.99;
    public static final double WEBSITE_BASED_MERCHANT_EXTRACTOR = 0.76;
    public static final double FIRST_LINE_BASED_MERCHANT_EXTRACTOR = 0.75;
    public static final double FONT_SIZE_BASED_MERCHANT_EXTRACTOR = 0.751;
    public static final double TAGLINE_BASED_MERCHANT_EXTRACTOR = 0.76;
    public static final double BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR = 0.95;
    public static final double ADDRESS_BASED_MERCHANT_EXTRACTOR = 0.751;
    public static final double NEAR_BY_VICINITY_EXTRACTOR = 0.6;
    public static final double REGEX_EXTRACTOR = 0.7;


    private ConfidenceValueCollection()
    {}
}
