package ai.infrrd.idc.receipt.fieldextractor.merchantname.config;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.service.ConfigService;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckClient;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MongoConnector;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.Utils;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.WordLookupClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApplicationConfiguration
{

    @Bean
    public MerchantNameExtractionUtil getMerchantNameExtractionUtil()
    {
        return new MerchantNameExtractionUtil();
    }


    @Bean
    public Utils getUtils()
    {
        return new Utils();
    }


    @Bean
    public ConfigService getConfigService()
    {
        return new ConfigService();
    }


    @Bean
    public MongoConnector getMongoConnector()
    {
        return new MongoConnector();
    }


    @Bean
    public SpellCheckClient getSpellCheckClient()
    {
        return new SpellCheckClient();
    }


    @Bean
    public WordLookupClient getWordLookupClient()
    {
        return new WordLookupClient();
    }
}
