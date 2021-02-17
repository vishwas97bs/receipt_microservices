package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantConstants {
    private MerchantConstants()
    {
    }


    public static final String REGEX_POS_LOOKAHEAD = "(?<=({0}).{0,10}:?)";
    public static final List<String> REGEX_POS_LOOKAHEAD_LIST = Arrays
            .asList( "(?<=({0}).{0,10}:?)", "(?<=({0}).{0,20}:?)", "(?<=({0}).{0,25}:?)", "(?<=({0}).{0,30}:?)" );
    public static final String REGEX_POS_VICINITY = "{0}";
    public static final String REGEX_POS_MONTHS = "{months}";

    public static final String REGEX_POS_CURRENCIES = "{currencies}";

    public static final String REGEX_POS_TAG_LINE = "{tag-line}";
    public static final String REGEX_POS_SUFFIX_LINE = "{suffix-line}";
    public static final String REGEX_POS_SUPER_DOMAIN = "{super-domain}";
    //Regex helper constants
    public static final String VISION_URL = "https://s.infrrdapis.com/upload";
    public static final String STAGING_API_KEY = "5a6350e0-f384-4712-b182-aef72fc0e51d";
    public static final String ABBYY_URL = "http://abbyy-lb-589625937.us-west-2.elb.amazonaws.com/abbyy/v1/processocr";
    //Default field value for mapping to id
    public static final String DEF_FIELD_VALUE = "unidentified";
    public static final String EXTRACT_REGEX_EXTENSION = "_extract_regex";
    public static final String LIST_EXTENSION = "_list";
    public static final String VICINITY_EXTENSION = "_vicinity";
    public static final String EXTRA_MONTHS_EXTENSION = "_extra_months";
    public static final String EXTRA_SHORT_MONTHS_EXTENSION = "_extra_short_months";
    public static final String CURRENCY = "currency";
    public static final String MERCHANT_NAME_WEBSITE = "merchant_name_website";
    public static final String MERCHANT_NAME_WEBSITE_DOMAINS = "merchant_name_website_domains";
    public static final String MERCHANT_NAME_WEBSITE_SECOND_LEVEL_DOMAINS = "merchant_name_website_second_level_domains";
    public static final String DEF_FIELD_NAME = "fieldName";
    public static final String DEF_PRE_PROCESSOR = "preProcessors";
    public static final String DEF_CANDIDATE_VAL_EXT = "candidateValueExtractors";
    public static final String DEF_FILTERS = "filters";
    public static final String DEF_FALLBACK = "fallback";
    public static final String DEF_SEQ_COMPONENT_ID = "seqComponentId";
    public static final String DEF_EXTRACTORS = "extractors";
    public static final String DEF_SUMMARIZER = "summarizers";
    public static final String DEF_DATA_ENRICHMENT = "dataEnrichment";
    public static final String DEF_RERUNNER = "rerunners";
    public static final String DEF_DEFAULT_FIELDS = "defaultList";
    public static final String DEF_RERUNNER_ITEM = "rerunners";
    public static final String DEF_BASE_FIELD = "summarizerBaseField";
    public static final String DATA_OCR_TEXT = "ocrText";
    public static final String DATA_OCR_XML = "ocrXml";
    public static final String DATA_LOCALE = "locale";
    public static final String DATA_SCAN_REQUEST = "scanRequest";
    public static final String PARALLELISM_HINT = "parallelismHint";
    public static final String MAX_SPOUT_PENDING = "maxSpoutPending";
    public static final String DEF_DATA_CORRECTORS = "dataCorrectors";
    public static final String DUMMY_SCANREQUEST_ID = "1234";
    public static final String DEF_RERUNNER_COMPONENT_ID = "rerun";
    public static final String DEF_BANNED_TEXT_PROCESSING = "textProcessing";
    public static final String BANNED_LANGUAGES_REGEX = "banned_languages_regex";
    public static final String NON_STRICT_VICINITY_EXTENSION = "_non_strict_vicinity";
    public static final String CONFIGURATION = "configuration";
    public static final String API_CONFIG = "apiConfig";
    public static final String LANGUAGES = "languages";
    public static final String RECEIPT = "receipt";
    public static final String GIMLET_CONFIGURATION = "gimletConfiguration";

    public static final Map<String, String> LANGUAGE_CODES = new HashMap<String, String>()
    {
        {
            put( "zh", "china" );
            put( "ko", "korean" );
            put( "ja", "japan" );
            put( "ar", "arabic" );
            put( "kn-IN", "india_Kannada" );
            put( "te-IN", "india_Telugu" );
            put( "ru", "russian" );
            //other indian languages can be added in the same format as above ,
            // google for language codes and refer map-values from gimletConfiguration.json/banned_languages_regex
        }

        ;
    };

    public static final Map<String, String> CUSTOM_LANGUAGE_DATE_REGEX = new HashMap<String, String>()
    {
        {
            put( "ru", "[а-яА-Я]+" );
        }
    };

    public static final Map<String, List<String>> CUSTOM_LANGUAGE_DATE_FORMAT = new HashMap<String, List<String>>()
    {
        {
            put( "ru", Arrays.asList( "ddMMMMuuuu", "dMMMMuuuu", "dd-MMM-uuuu", "ddMMMuuuu", "ddMMMuu" ) );
        }
    };

    public static final double BOOSTED_CONFIDENCE = 0.69;
}
