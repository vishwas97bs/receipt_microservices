package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MongoConnector;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DBCollection;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GimletConfigService implements InitializingBean {

    @Value("${dbName}")
    private String dbName;

    @Value("${spellcheck-server}")
    private String spellCheckUrl;

    @Value ( "${mongoServer}")
    private String mongoServer;

    @Override
    public void afterPropertiesSet() throws Exception {
        MongoConnector.initializeMongoUrl(mongoServer);
    }

    public Map<String, Object> getGimletConfig(){
        ObjectMapper mapper = new ObjectMapper();
        DBCollection gimletConfiguration = null;
        gimletConfiguration = MongoConnector.getDB( dbName)
                .getCollection(MerchantConstants.GIMLET_CONFIGURATION);
        Object configObject = null;
        if (gimletConfiguration!=null){
             configObject = gimletConfiguration.findOne(MerchantConstants.RECEIPT);
        }else{
            return null;
        }
        JSONObject configJsonObj = null;
        Map<String,Object> map = null;
        try {
            configJsonObj = mapper.readValue(configObject.toString(), JSONObject.class);
            map = mapper.readValue(configJsonObj.toString(),HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return map;
    }

}
