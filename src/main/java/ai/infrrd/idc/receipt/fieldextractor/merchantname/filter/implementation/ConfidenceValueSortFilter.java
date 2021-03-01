package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface.ExtractionFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Component
public class ConfidenceValueSortFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( ConfidenceValueSortFilter.class );


    @Override
    public List<ExtractedValue> filter( List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest )
    {
        if ( input != null && !input.isEmpty() ) {
            Collections.sort( input, new ConfidenceValueComparator() );
            LOG.trace( "List after filteration: {}", input );
        }
        return input;
    }
}
