package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface.ExtractionFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.ConfidenceValueCollection;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.UtilConditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MerchantNameFinalizerFilter implements ExtractionFilter {
    private static final Logger LOG = LoggerFactory.getLogger( MerchantNameFinalizerFilter.class );


    @Override
    public List<ExtractedValue> filter(List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest)
    {
        LOG.trace( "Entering filter method" );
        if ( input == null ) {
            LOG.warn( "Input list was found to be null" );
            return new ArrayList<>();
        }
        // If we have Google Vision/Existing Merchant extraction, we pick it as-is
        if ( !input.isEmpty() && ( input.get( 0 ).getOperation().equals( MerchantNameExtractorType.GOOGLE_VISION )
                || input.get( 0 ).getOperation().equals( MerchantNameExtractorType.EXISTING_MERCHANT ) || input.get( 0 )
                .getOperation().equals( MerchantNameExtractorType.BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR ) || input.get( 0 )
                .getOperation().equals( MerchantNameExtractorType.TRAINING_BASED ) || input.get( 0 )
                .getOperation().equals( MerchantNameExtractorType.CUSTOMER_KNOWN_MERCHANT ) ) ) {
            LOG.warn( "Returning the filter without any change to input" );
            return input;
        }
        List<ExtractedValue> outputList = new ArrayList<>( input );
        updateConfidenceByOccurrence( outputList );
        Collections.sort( outputList, new ConfidenceValueComparator() );
        LOG.trace( "List after filteration: {}", outputList );
        return outputList;
    }


    /**
     * this method will apply rules to update confidence of candidate values in input
     * @param input
     */
    private void updateConfidenceByOccurrence( List<ExtractedValue> input )
    {
        LOG.trace( "Entering updateConfidenceByOccurrence method" );
        List<String> highestOccurrenceList = updateHighestOccurrenceList( input );

        if ( highestOccurrenceList.size() != input.size() ) {
            String selectedVal = getValueWithHighestSubstringOccurrence( highestOccurrenceList, input );
            if ( !StringUtils.isEmpty( selectedVal ) ) {
                for ( ExtractedValue val : input ) {
                    if ( ( (String) val.getValue() ).equalsIgnoreCase( selectedVal ) ) {
                        // Should have higher confidence than the first line selector
                        double confidence = MerchantNameExtractionUtil.getConfidence( selectedVal,
                                ConfidenceValueCollection.FIRST_LINE_BASED_MERCHANT_EXTRACTOR + .04, 0 );
                        LOG.trace( "Updating confidence of {} to {}", val.getValue(), confidence );
                        val.setConfidence( confidence );
                    }
                }
            }
        }
        LOG.trace( "Finishing updateConfidenceByOccurrence method" );
    }


    /*
     * this method is used to find if any value is contained in another value
     */
    private String getValueWithHighestSubstringOccurrence( List<String> values, List<ExtractedValue> candidates )
    {
        LOG.trace( "Entering getValueWithHighestSubstringOccurrence method" );
        String selectedVal = null;
        if ( values.size() > 1 ) {
            int highestOccurrenceCount = 0;
            for ( String val : values ) {
                List<String> operationList = new ArrayList<>();
                int occuranceCount = getValueSubstringExistenceCount( val, candidates, operationList );
                if ( occuranceCount >= highestOccurrenceCount && checkOperationAuthenticity( operationList ) ) {
                    selectedVal = val;
                    highestOccurrenceCount = occuranceCount;
                }
            }
        } else {
            if ( values.size() == 1 )
                selectedVal = values.get( 0 );
        }
        LOG.trace( "Value with highest substring occurence: {}", selectedVal );
        return selectedVal;
    }


    /**
     * this method is used to find the existence count of a value in a list
     * @param extractedVal value to be searched
     * @param list list of value where extractedVal need to be searched
     * @param fuzzy (defines weather to match fuzzily)
     * @param operationList (list of operation of matched value this value will be manipulated in this method)
     * @return
     */
    private int checkValueExistenceCount( ExtractedValue extractedVal, List<ExtractedValue> list, boolean fuzzy,
                                          List<String> operationList )
    {
        LOG.trace( "Entering checkValueExistenceCount method" );
        String value = (String) extractedVal.getValue();
        int response = 0;
        for ( ExtractedValue extVal : list ) {
            if ( fuzzy ) {
                String val1 = value.replaceAll( " ", "" ).toLowerCase();
                String val2 = ( (String) extVal.getValue() ).replaceAll( " ", "" ).toLowerCase();
                int distance = StringUtils.getLevenshteinDistance( val1, val2,
                        (int) Math.ceil( val1.length() * UtilConditions.THRESHOLD_LEVENSHTEIN ) );

                if ( distance < 0 ) {
                    continue;
                } else {
                    if ( distance == 0 || !extractedVal.getOperation().equals( MerchantNameExtractorType.WEBSITE_DOMAIN ) ) {
                        response++;
                        operationList.add( extVal.getOperation() );
                    }
                }
            } else {
                if ( ( (String) extVal.getValue() ).equalsIgnoreCase( value ) && extVal.getConfidence() > response ) {
                    response++;
                    operationList.add( extVal.getOperation() );

                }
            }
        }
        LOG.trace( "Finishing checkValueExistenceCount method" );
        return response;
    }


    private int getValueSubstringExistenceCount( String value, List<ExtractedValue> list, List<String> operation )
    {
        LOG.trace( "Entering getValueSubstringExistenceCount method" );
        int response = 0;
        String valueToCheck = value.toLowerCase().replaceAll( "[\\s]+", "\\s" );
        for ( ExtractedValue extVal : list ) {
            String compareVal = ( (String) extVal.getValue() ).replaceAll( "[\\s]+", "\\s" ).toLowerCase();
            if ( compareVal.contains( valueToCheck ) ) {
                response++;
                if ( !operation.contains( extVal.getOperation() ) )
                    operation.add( extVal.getOperation() );
            }
        }
        LOG.trace( "Finishing getValueSubstringExistenceCount method" );
        return response;
    }


    /**
     * this method will return list of values with highest occurrence
     * @param candidates
     * @return
     */
    private List<String> updateHighestOccurrenceList( List<ExtractedValue> candidates )
    {
        LOG.trace( "Entering updateHighestOccurrenceList method" );
        Map<String, Integer> occurrenceMap = new HashMap<>();
        int highestOccurrence = 0;
        for ( ExtractedValue value : candidates ) {
            if ( !occurrenceMap.containsKey( value.getValue() ) ) {
                List<String> operationList = new ArrayList<>();
                int occurrenceCount = checkValueExistenceCount( value, candidates, true, operationList );
                if ( occurrenceCount > highestOccurrence && checkOperationAuthenticity( operationList ) )
                    highestOccurrence = occurrenceCount;
                occurrenceMap.put( (String) value.getValue(), occurrenceCount );
            }
        }
        List<String> highestOccurrenceList = new ArrayList<>();
        if ( highestOccurrence > 1 ) {
            for ( Map.Entry<String, Integer> occurrence : occurrenceMap.entrySet() ) {
                if ( occurrence.getValue() == highestOccurrence ) {
                    highestOccurrenceList.add( occurrence.getKey() );
                }
            }
        }
        LOG.trace( "Finishing updateHighestOccurrenceList method" );
        return highestOccurrenceList;
    }


    /*
     * this method will contain all rules for up voting any candidate value
     *
     */
    private boolean checkOperationAuthenticity( List<String> operationList )
    {
        LOG.trace( "Entering checkOperationAuthenticity method" );
        boolean response = true;
        if ( operationList.size() == 2
                && ( operationList.contains( MerchantNameExtractorType.POSSIBLE_MERCHANT_NAME_BASED_ON_FONT_SIZE )
                && operationList.contains( MerchantNameExtractorType.POSSIBLE_MERCHANT_NAME_FROM_TOP ) ) ) {
            //only first line based and size based extraction can not be used to evaluate candidate value above others
            response = false;
        }
        LOG.trace( "Finishing checkOperationAuthenticity method" );
        return response;
    }
}
