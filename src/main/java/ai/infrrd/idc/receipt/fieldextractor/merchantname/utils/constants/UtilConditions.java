package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants;

public class UtilConditions {
    private UtilConditions()
    {}

    public static final int THRESHOLD_LENGTH = 3;
    public static final double THRESHOLD_LEVENSHTEIN = 0.10;
    public static final double SCORE_INITIAL = .99;
    public static final double SCORE_DECAY = 0.90;
    public static final int THRESHOLD_MERCHANT_NAME_NGRAM = 3;
    public static final int THRESHOLD_STOPWORD_FUZZY_MATCH = 1;

    public static final int MIN_LENGTH_LEVENSHTEIN = 3;

    public static final int MAX_NGRAM_SIZE = 5;

    public static final int MIN_NGRAM_SIZE = 2;
}
