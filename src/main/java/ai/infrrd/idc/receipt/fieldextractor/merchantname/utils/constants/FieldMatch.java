package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants;

public enum  FieldMatch {
    REG_EX_MATCH,
    VICINITY_MATCH,
    NEG_VICINITY_MATCH,
    LIST_MATCH,
    TAG_LINE,
    SUFFIX_LINE,
    REG_WITHOUT_VIC,
    NEARBY_VICINITY_MATCH,
    LOCALE_BASED_MATCH,
    DOUBLE_REGEX_MATCH,
    LANGUAGE_WORDS_MATCH,
    REGION_REGEX_MATCH,
    CUSTOMER_KNOWN_MERCHANT
}
