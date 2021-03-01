package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.commons.entities.FieldExtractionResponse;
import ai.infrrd.idc.spellcheckutility.entities.response.LocaleDetectionResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LocaleService implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger( LocaleService.class );
    private final Map<String, String> hashMap = new HashMap<>();


    @Override
    public void afterPropertiesSet() {
        hashMap.put( "eng", "en" );
        hashMap.put( "spa", "es" );
        hashMap.put( "fra", "fr" );
        hashMap.put( "ita", "it" );
        hashMap.put( "deu", "de" );
        hashMap.put( "pol", "pl" );
    }

    //conversion of tabular data into localeDetectionResponseEntity
    public String conversionOfLocale( FieldExtractionRequest request )
    {
        String localeString = "en_us";

        LocaleDetectionResponseEntity locale = new LocaleDetectionResponseEntity();
        List<List<FieldExtractionResponse>> localeList = new ArrayList<>();
        List<FieldExtractionResponse> fieldExtractionResponse = request.getExtractedFields();
        for ( FieldExtractionResponse fieldExtractionResponse1 : fieldExtractionResponse ) {
            if ( fieldExtractionResponse1.getFieldName().equalsIgnoreCase( "locale" ) ) {
                localeList = fieldExtractionResponse1.getValues();
            }
        }

        if ( localeList != null && localeList.size() > 0 && localeList.get( 0 ) != null && localeList.get( 0 ).size() > 0 ) {
            List<FieldExtractionResponse> response = localeList.get( 0 );
            for ( FieldExtractionResponse index : response ) {

                if ( index.getFieldName().equalsIgnoreCase( "countrycode" ) ) {
                    locale.setCountryCode( (String) index.getValue() );
                } else if ( index.getFieldName().equalsIgnoreCase( "countryname" ) ) {
                    locale.setCountryName( (String) index.getValue() );
                } else if ( index.getFieldName().equalsIgnoreCase( "city" ) ) {
                    locale.setCity( (String) index.getValue() );
                } else if ( index.getFieldName().equalsIgnoreCase( "language" ) ) {
                    locale.setLanguage( (String) index.getValue() );
                } else if ( index.getFieldName().equalsIgnoreCase( "localevalue" ) ) {
                    locale.setLocaleValue( (String) index.getValue() );
                }
            }
            localeString = locale.getLocaleValue();
        } else {
            LOGGER.warn( "Locale is Empty" );
            localeString = "en_us";
        }

        return localeString;
    }


}
