package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.MerchantnameApplication;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigService {

    private static ConfigService configService = null;

    private ConfigService(){

    }

    public static ConfigService getInstance(){
        if (configService==null){
            configService = new ConfigService();
        }
        return  configService;
    }



    public List<String> getRegexList(String fieldName , Map<String,Object> config)
    {
        return getAllValueList( fieldName, MerchantConstants.EXTRACT_REGEX_EXTENSION,config );
    }

    @SuppressWarnings ("unchecked") public List<String> getAllValueList( String key, String extension , Map<String,Object> config)
    {
        List<String> response = new ArrayList<>();

        List<String> keySet = getKeys( key, extension ,new HashMap<>());

        for ( String prop : keySet ) {
            List<String> value = (List<String>) getExtractionConfiguration( prop,config );
            if ( value != null )
                response.addAll( new ArrayList<>( value ) );
        }
        return response;
    }

    String locale;

    private List<String> getKeys(String key, String extension, Map<String ,Object> configuration )
    {
        List<String> response = new ArrayList<>();
        if ( this.locale != null ) {
            if ( this.locale.contains( "_" ) ) {
                String value = this.locale + "_" + key + extension;
                response.add( value );
            } else {
                String valuePattern = this.locale + ".{0,9}_" + key + extension;
                for ( String ky : configuration.keySet() ) {
                    if ( ky.matches( valuePattern ) ) {
                        response.add( ky );
                    }
                }
            }
        }
        if ( response.isEmpty() ) {
            String value = key + extension;
            response.add( value );
        }
        return response;
    }

    @SuppressWarnings ("unchecked") public List<String> getValueList( String key,Map<String ,Object> configuration  )
    {
        List<String> response = (List<String>) getExtractionConfiguration( key,configuration );
        if ( response == null )
            response = new ArrayList<>();
        return new ArrayList<>( response );
    }

    public Object getExtractionConfiguration( String key,Map<String ,Object> configuration )
    {
        Object value = null;
        Map<String,Object> map = (Map<String, Object>) configuration.get("configuration");
        if ( map.containsKey(key)){
            value = map.get(key);
        }
        return value;
    }
}
