package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.commons.entities.FieldExtractionResponse;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.ExistingMerchantNameExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.MerchantNameFromTopOfTextExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MongoConnector;
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

    @Override
    public void afterPropertiesSet() throws Exception {
        MongoConnector.initializeMongoUrl(mongoServer);
        MerchantNameExtractionUtil.initializeMongo(dbName);
    }

    public List<List<FieldExtractionResponse>> extractMerchantName(FieldExtractionRequest fieldExtractionRequest){
        List<List<FieldExtractionResponse>> listOfResponses = new ArrayList<>();
        List<FieldExtractionResponse> responses = new ArrayList<>();
        Map<String,Object> configMap = getGimletConfig();
        List<ExtractedValue> values = merchantNameFromTopOfTextExtractor.extractValue(fieldExtractionRequest,"merchantname",configMap);
        List<ExtractedValue> values1  = existingMerchantNameExtractor.extractValue(fieldExtractionRequest,"existingmerchant",configMap);
        for (ExtractedValue extractedValue:values){
            FieldExtractionResponse fieldExtractionResponse = new FieldExtractionResponse();
            fieldExtractionResponse.setValue(extractedValue.getValue());
            fieldExtractionResponse.setOperation(extractedValue.getOperation());
            fieldExtractionResponse.setConfidence(extractedValue.getConfidence());
            if (!fieldExtractionResponse.getValue().toString().isEmpty()){
                fieldExtractionResponse.setSuccess(true);
            }
            responses.add(fieldExtractionResponse);
        }
        FieldExtractionResponse fieldExtractionResponse = new FieldExtractionResponse();
        fieldExtractionResponse.setSuccess(true);
        fieldExtractionResponse.setValue(values1.get(0).getValue());
        fieldExtractionResponse.setConfidence(values1.get(0).getConfidence());
        fieldExtractionResponse.setOperation(values1.get(0).getOperation());
        responses.add(fieldExtractionResponse);
        listOfResponses.add(responses);
        return listOfResponses;
    }

    public Map<String,Object> getGimletConfig(){
        Map<String,Object> configMap = gimletConfigService.getGimletConfig();
        if (configMap == null){
            return new HashMap<>();
        }
        return  configMap;
    }




}
