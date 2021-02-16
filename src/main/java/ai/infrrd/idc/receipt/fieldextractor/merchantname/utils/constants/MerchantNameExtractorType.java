package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants;

public class MerchantNameExtractorType {
    public static final String EXISTING_MERCHANT = "EXISTING_MERCHANT_NAME";
    public static final String GOOGLE_VISION = "GOOGLE_VISION_MERCHANT_NAME";
    public static final String WEBSITE_DOMAIN = "WEBSITE_DOMAIN_MERCHANT_NAME";
    public static final String POSSIBLE_MERCHANT_NAME_FROM_TOP = "POSSIBLE_MERCHANT_NAME_FROM_TOP";
    public static final String POSSIBLE_MERCHANT_NAME_BASED_ON_FONT_SIZE = "POSSIBLE_MERCHANT_NAME_BASED_ON_FONT_SIZE";
    public static final String BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR = "BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR";
    public static final String BLOCK_BASED = "BLOCK_BASED_MERCHANT_NAME";
    public static final String TRAINING_BASED = "TRAINING_BASED";
    public static final String TAG_LINE = "TAG_LINE";
    public static final String SUFFIX_LINE = "SUFFIX_LINE";
    public static final String  CUSTOMER_KNOWN_MERCHANT= "CUSTOMER_KNOWN_MERCHANT";

    private MerchantNameExtractorType()
    {}
}
