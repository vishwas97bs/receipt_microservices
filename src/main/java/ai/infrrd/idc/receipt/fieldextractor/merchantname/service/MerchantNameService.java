package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.commons.entities.FieldExtractionResponse;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.ExistingMerchantNameExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.MerchantNameFromTopOfTextExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.MerchantNamePrefixNSuffixBasedExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.WebsiteDomainMerchantNameExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MongoConnector;
import ai.infrrd.idc.utils.entity.ValueIndex;
import ai.infrrd.idc.utils.extractors.IndexExtractor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MerchantNameService implements InitializingBean {

    @Value("${dbName}")
    private String dbName;

    @Value("${spellcheck-server}")
    private String spellCheckUrl;

    @Value ( "${mongoServer}")
    private String mongoServer ;

    @Autowired
    private ExistingMerchantNameExtractor existingMerchantNameExtractor;

    @Autowired
    private MerchantNameFromTopOfTextExtractor merchantNameFromTopOfTextExtractor;

    @Autowired
    private GimletConfigService gimletConfigService;

    @Autowired
    private WebsiteDomainMerchantNameExtractor websiteDomainMerchantNameExtractor;

    @Autowired
    private MerchantNamePrefixNSuffixBasedExtractor merchantNamePrefixNSuffixBasedExtractor;

    @Autowired
    private PreprocessorService preprocessorService;

    @Autowired
    private FilterService filterService;

    @Autowired
    private LocaleService localeService;

    @Override
    public void afterPropertiesSet() throws Exception {
        MongoConnector.initializeMongoUrl(mongoServer);
        MerchantNameExtractionUtil.initializeMongo(dbName);
    }

    public List<List<FieldExtractionResponse>> extractMerchantName(FieldExtractionRequest fieldExtractionRequest){
        String locale = localeService.conversionOfLocale( fieldExtractionRequest );
        String preprocessedText = preprocessorService.preprocess(fieldExtractionRequest,"merchantname");
        fieldExtractionRequest.getOcrData().setRawText(preprocessedText);
        List<List<FieldExtractionResponse>> listOfResponses = new ArrayList<>();
        List<FieldExtractionResponse> responses = new ArrayList<>();
        Map<String,Object> configMap = getGimletConfig();
        List<ExtractedValue> prefixSuffixMerchant = merchantNamePrefixNSuffixBasedExtractor.extractValue(fieldExtractionRequest,"merchantName",configMap,locale);
        List<ExtractedValue> websiteExtractedMerchant = websiteDomainMerchantNameExtractor.extractValue(fieldExtractionRequest,"merchantname",configMap);
        List<ExtractedValue> topOfTextMerchant = merchantNameFromTopOfTextExtractor.extractValue(fieldExtractionRequest,"merchantname",configMap);
        List<ExtractedValue> existingmerchant  = existingMerchantNameExtractor.extractValue(fieldExtractionRequest,"existingmerchant",configMap);

        List<ExtractedValue> listOfValues = new ArrayList<>();
        addResponses(prefixSuffixMerchant,listOfValues);
        addResponses(topOfTextMerchant,listOfValues);
        addResponses(websiteExtractedMerchant,listOfValues);
        addResponses(existingmerchant,listOfValues);

        List<ExtractedValue> filteredValues = filterService.filterProcess(listOfValues,fieldExtractionRequest);
        IndexExtractor indexExtractor = new IndexExtractor();

       FieldExtractionResponse fieldExtractionResponse = new FieldExtractionResponse();
        if (filteredValues!=null && filteredValues.size()>0){
            fieldExtractionResponse.setValue(filteredValues.get(0).getValue());
            fieldExtractionResponse.setConfidence(filteredValues.get(0).getConfidence());
            fieldExtractionResponse.setOperation(filteredValues.get(0).getOperation());
            fieldExtractionResponse.setMatchedVicinity(filteredValues.get(0).getMatchedVicinity());
            fieldExtractionResponse.setFieldName(fieldExtractionRequest.getFieldConfigDetails().get(0).getFieldName());
            fieldExtractionResponse.setSuccess(true);
            //index extraction
            if ( filteredValues.get(0).getMatchedValue() != null
                    && !filteredValues.get(0).getMatchedValue().isEmpty() ) {
                ValueIndex indexes = indexExtractor.getValueIndex( fieldExtractionRequest.getOcrData().getRawText(),
                        filteredValues.get(0).getMatchedValue(), filteredValues.get(0).getMatchedVicinity() );
                fieldExtractionResponse.setStartIndex( indexes.getStartIndex() );
                fieldExtractionResponse.setEndIndex( indexes.getEndIndex() );
            }else{
                ValueIndex indexes = indexExtractor.getValueIndex( fieldExtractionRequest.getOcrData().getRawText(),
                        filteredValues.get(0).getValue().toString(), filteredValues.get(0).getMatchedVicinity() );
                fieldExtractionResponse.setStartIndex( indexes.getStartIndex() );
                fieldExtractionResponse.setEndIndex( indexes.getEndIndex() );
            }
        }

        responses.add(fieldExtractionResponse);
        listOfResponses.add(responses);
        return listOfResponses;
    }

    //fetch configuration(regexes and vicinities stored in mongodb)
    public Map<String,Object> getGimletConfig(){
        Map<String,Object> configMap = gimletConfigService.getGimletConfig();
        if (configMap == null){
            return new HashMap<>();
        }
        return  configMap;
    }

    //add all respones from extractors
    public List<ExtractedValue> addResponses(List<ExtractedValue> response,List<ExtractedValue> responses){
        for (ExtractedValue extractedValue:response){
            responses.add(extractedValue);
        }
        return responses;
    }

}
