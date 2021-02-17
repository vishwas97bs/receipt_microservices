package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.commons.entities.FieldExtractionResponse;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckException;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.MerchantNameExtractionUtil;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.Utils;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.WordLookupClient;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.WordLookupRequestEntity;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.ConfidenceValueCollection;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;


@Component
public class MerchantNameFromTopOfTextExtractor implements CandidateValueExtractor, InitializingBean
{

    private static final Logger LOG = LoggerFactory.getLogger( MerchantNameFromTopOfTextExtractor.class );

    private static  WordLookupClient WORD_LOOKUP_CLIENT = null;

    private MerchantNameExtractionUtil merchantNameExtractionUtil = MerchantNameExtractionUtil.getInstance();

    private Utils utils= Utils.getInstance();

    @Value("${spellcheck-server}")
    private String spellCheckUrl;

    @Override
    public void afterPropertiesSet() throws Exception {
        if ( WORD_LOOKUP_CLIENT == null){
            WORD_LOOKUP_CLIENT = new WordLookupClient(
                    spellCheckUrl );
        }
    }

    @Override
    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName,Map<String,Object> config )
    {

        String ocrText = feRequest.getOcrData().getRawText();
        List<String> text = utils.getStringLines( ocrText );
        List<ExtractedValue> extractedValueList = new ArrayList<>();
        ExtractedValue extractedMerchantName;
        String merchantName = null;

        // Remove obvious bad lines of text
        List<String> cleanLines = merchantNameExtractionUtil.filter( merchantNameExtractionUtil.lightClean( text ) );

        LOG.trace( "Cleaned Lines : {}", cleanLines );
        if ( cleanLines.isEmpty() ) {
            // Couldn't find any useful lines
            return Collections.emptyList();
        } else {
            // Extract from one of the first 5 lines or 30% of the text in the
            // text
            int linesToTry = Math.min( 5, (int) Math.ceil( 0.3 * cleanLines.size() ) );
            for ( int i = 0; i < linesToTry; i++ ) {
                // Extract the potential name from the text, use website domain
                // to aid extraction
                String val = cleanLines.get( i );
                LOG.debug( "Getting Potential Name from : {}", val );
                String potentialName = merchantNameExtractionUtil.extractPotentialName( val );
                if ( potentialName != null ) {
                    LOG.debug( "Got Potential Name : {}", potentialName );
                    merchantName = potentialName;
                    break;
                }
            }
        }
        /*
         * TODO: Operations,index and confidence value might have to be changed
         * in the extractedMerchantName variable.
         */
        double baseScore = ConfidenceValueCollection.FIRST_LINE_BASED_MERCHANT_EXTRACTOR;
        String operation = MerchantNameExtractorType.POSSIBLE_MERCHANT_NAME_FROM_TOP;
        List<String> possibleNames = new ArrayList<>();
        if ( merchantName != null && !merchantName.isEmpty() )
            possibleNames.add( merchantName );
        LOG.debug( "Making word lookup for : {}", possibleNames );
        if ( possibleNames.size() != 0 ) {
            Map<String, Map<String, Object>> wordLookupResponse = wordLookup( possibleNames );
            LOG.trace( "Got response from word lookup : {}", wordLookupResponse );
            if ( wordLookupResponse.containsKey( merchantName ) ) {
                Map<String, Object> merchantNameVal = wordLookupResponse.get( merchantName );
                int validWordCount = 0;
                for ( Object valmerchantNameVal : merchantNameVal.values() ) {
                    if ( (boolean) valmerchantNameVal ) {
                        validWordCount++;
                    }
                }
                LOG.debug( "Best Guess information for value : {} word details : {}", merchantName, merchantNameVal );
                if ( validWordCount == merchantNameVal.size() ) {
                    baseScore = ConfidenceValueCollection.BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR;
                    operation = MerchantNameExtractorType.BEST_GUESS_LOOKUP_BASED_MERCHANT_EXTRACTOR;
                }
            }
        }

        FieldExtractionResponse fieldExtractionResponse = new FieldExtractionResponse();
        extractedMerchantName = new ExtractedValue( merchantName, operation, null, 0, baseScore );
        if ( extractedMerchantName.getValue() != null ) {
            extractedValueList.add( extractedMerchantName );
        }
        LOG.debug( "Extracted:\n{}", extractedValueList );
        return extractedValueList;
    }


    public static Map<String, Map<String, Object>> wordLookup(List<String> lines )
    {
        LOG.debug( "Fetching merchant name suggestions for {}.", lines );
        try {
            WordLookupRequestEntity wordLookupRequestEntity = new WordLookupRequestEntity( "merchants", lines );
            return WORD_LOOKUP_CLIENT.wordLookAPI( wordLookupRequestEntity );
        } catch ( SpellCheckException e ) {
            LOG.error( "SpellCheckException while finding possible nGrams from lookup for {}", lines, e );
            return new HashMap<>();
        }
    }

}
