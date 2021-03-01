package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface.ExtractionFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.FieldMatch;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class RegexExtractorCandidateConfidenceBoostingFilter implements ExtractionFilter
{

    private static final Logger LOG = LoggerFactory.getLogger( RegexExtractorCandidateConfidenceBoostingFilter.class );
    private static final String BOOST_REGEX_MATCH = "enableRegexMatchBoost";
    private static final double BOOSTED_CONFIDENCE = MerchantConstants.BOOSTED_CONFIDENCE;


    @Override
    public List<ExtractedValue> filter( List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest )
    {
        LOG.trace( "List before filteration: {}", input );

        List<ExtractedValue> response = input;
        if ( input == null || input.isEmpty() ) {
            LOG.warn( "inputList cannot be null / empty" );
            LOG.trace( "Finishing filter method" );
            return Collections.emptyList();
        }

        List<ExtractedValue> vicinityMatchList = getExtractedValueByOperation( input, FieldMatch.VICINITY_MATCH.toString() );
        List<ExtractedValue> withoutVicinityMatchList = getExtractedValueByOperation( input,
            FieldMatch.REG_WITHOUT_VIC.toString() );
        List<ExtractedValue> regexMatchList = getExtractedValueByOperation( input, FieldMatch.REG_EX_MATCH.toString() );

        //        if ( withoutVicinityMatchList.size() == 1 ) {
        //            boostConfidence( withoutVicinityMatchList );
        //        }

        Boolean boostRegexMatch = Boolean.parseBoolean( "true" );
        LOG.info( "Enable Boost Regex Match: {}", boostRegexMatch );
        if ( boostRegexMatch && regexMatchList.size() == 1 && vicinityMatchList.isEmpty()
            && withoutVicinityMatchList.isEmpty() ) {
            boostConfidence( regexMatchList );
        }
        if ( boostRegexMatch && !withoutVicinityMatchList.isEmpty() && vicinityMatchList.isEmpty()
            && regexMatchList.isEmpty() ) {
            boostConfidence( withoutVicinityMatchList );
        }
        LOG.trace( "List after filteration: {}", response );
        return response;
    }


    private void boostConfidence( List<ExtractedValue> list )
    {
        LOG.trace( "Entering boostConfidence method" );
        for ( ExtractedValue value : list ) {
            value.setConfidence( BOOSTED_CONFIDENCE );
        }
        LOG.trace( "Finishing boostConfidence method" );
    }


    public static List<ExtractedValue> getExtractedValueByOperation( List<ExtractedValue> list, String operation )
    {
        LOG.trace( "Entering getExtractedValueByOperation method" );
        List<ExtractedValue> evList;

        if ( list == null || list.isEmpty() ) {
            LOG.error( "The list cannot be empty / null\nFound: {}", operation );
            return list;
        }

        if ( StringUtils.isBlank( operation ) ) {
            LOG.error( "The operation cannot be null / empty\nFound: {}", operation );
        }

        evList = new ArrayList<>();
        for ( ExtractedValue ev : list ) {
            if ( ev.getOperation().equalsIgnoreCase( operation ) ) {
                evList.add( ev );
            }
        }

        LOG.trace( "Finishing getExtractedValueByOperation method" );
        return evList;
    }
}
