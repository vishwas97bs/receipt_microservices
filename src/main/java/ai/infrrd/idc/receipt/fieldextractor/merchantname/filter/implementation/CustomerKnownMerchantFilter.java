package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface.ExtractionFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class CustomerKnownMerchantFilter implements ExtractionFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( CustomerKnownMerchantFilter.class );

    @Override
    public List<ExtractedValue> filter( List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest )
    {
        LOG.trace( "Entering filter method" );
        if ( input == null || input.isEmpty() ) {
            LOG.warn( "Input list found to be empty" );
            return input;
        }
        return input.stream().sorted( ( ExtractedValue value1, ExtractedValue value2 ) -> {
            if ( value1.getOperation().equals( MerchantNameExtractorType.CUSTOMER_KNOWN_MERCHANT ) )
                return -1;
            if ( value2.getOperation().equals( MerchantNameExtractorType.CUSTOMER_KNOWN_MERCHANT ) )
                return 1;
            return 0;
        } ).collect( Collectors.toList() );
    }
}
