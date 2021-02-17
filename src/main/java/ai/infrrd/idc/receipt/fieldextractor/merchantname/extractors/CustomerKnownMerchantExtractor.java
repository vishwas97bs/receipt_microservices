//package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;
//
//
//import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
//
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.FieldMatch;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class CustomerKnownMerchantExtractor implements CandidateValueExtractor {
//    private static final Logger LOG = LoggerFactory.getLogger( CustomerKnownMerchantExtractor.class );
//
//    @Override
//    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName,Map<String,Object> config) {
//        Map<String, Object> apiKeyConfiguration;
//        String rawText = feRequest.getOcrData().getRawText();
//        List<ExtractedValue> response = null;
//        if ( null != feRequest && null != feRequest.getApiKeyConfiguration() ) {
//            apiKeyConfiguration = feRequest.getApiKeyConfiguration();
//
//            if ( apiKeyConfiguration.containsKey( "configuration" ) && null != apiKeyConfiguration.get( "configuration" ) ) {
//                Map<String, Object> configuration;
//                configuration = (Map<String, Object>) apiKeyConfiguration.get( "configuration" );
//
//                if ( configuration.containsKey( "apiConfig" ) && null != configuration.get( "apiConfig" ) ) {
//                    Map<String, Object> apiConfig;
//                    apiConfig = (Map<String, Object>) configuration.get( "apiConfig" );
//                    LOG.info( "APIConfig for customer known merchant approach {}", apiConfig.toString() );
//                    Map<String, Object> customerConfig = null;
//
//                    if ( apiConfig.containsKey( "customerConfig" ) && null != apiConfig.get( "customerConfig" ) ) {
//                        customerConfig = (Map<String, Object>) apiConfig.get( "customerConfig" );
//                        Boolean enableCustomer = (Boolean) customerConfig.get( "enableMerchant" );
//                        if ( enableCustomer ) {
//                            LOG.info("fetching customer known merchant for {}",feRequest.getRequestId());
//                            response = findMerchantName( feRequest );
//                            LOG.info("found mechant {}",response);
//                        }
//                    }
//                }
//            }
//        }
//
//        return response;
//    }
//
//    private List<ExtractedValue> findMerchantName(FieldExtractionRequest feRequest )
//    {
//        LOG.info( "Entered find Merchant name" );
//        Map<String, List<String>> customerMerchantMap=(Map<String, List<String>>)feRequest.getConfiguration().get("customer_merchant_map");
//        String rawtext=feRequest.getOcrData().getRawText().toLowerCase().replaceAll("  "," ");
//
//        return customerMerchantMap.keySet().stream().filter(key->{
//            return customerMerchantMap.get(key).stream()
//                    .anyMatch(text->
//                    {
//                        return rawtext.contains(text) ;
//                    });
//        })
//                .map(key->{
//                    ExtractedValue extractedValue=new ExtractedValue();
//                    extractedValue.setConfidence(1.0);
//                    extractedValue.setValue(key);
//                    extractedValue.setOperation(FieldMatch.CUSTOMER_KNOWN_MERCHANT.toString());
//                    return extractedValue;
//        }).collect(Collectors.toList());
//    }
//}
