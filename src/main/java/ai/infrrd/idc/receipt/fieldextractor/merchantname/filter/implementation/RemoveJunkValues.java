package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface.ExtractionFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class RemoveJunkValues implements ExtractionFilter {
    private static final Logger LOG = LoggerFactory.getLogger( RemoveJunkValues.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest )
    {
        LOG.trace( "Entering filter method" );
        List<ExtractedValue> outputList = new ArrayList<>();

        if ( input != null && !input.isEmpty() ) {
            for ( ExtractedValue ext : input ) {
                String val = ext.getValue().toString();
                if ( toBeAdded( val ) ) {
                    outputList.add( ext );
                }
            }
        }

        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }


    private static boolean toBeAdded( String val )
    {
        int counter = 0;
        for ( int i = 0; i < val.length(); i++ ) {
            if ( val.charAt( i ) != ' ' ) {
                counter++;
            } else {
                counter = 0;
            }
            if ( counter >= 20 ) {
                return false;
            }
        }
        return true;
    }
}
