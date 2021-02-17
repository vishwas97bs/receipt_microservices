package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckClient;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckException;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.SpellCheckRequestEntity;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.*;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.NGram;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.NGramComparator;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck.Tuple3;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.UtilConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;



/**
 * Extract name from existing merchants
 *
 * @author charu
 */
@Component
public class ExistingMerchantNameExtractor implements CandidateValueExtractor, InitializingBean
{

    private static final double MINIMUM_CONFIDENCE = .5;
    private static final double MAX_CONFIDENCE = .99;
    private static final Logger LOG = LoggerFactory.getLogger( ExistingMerchantNameExtractor.class );
    private static final String ENABLE_N_GRAM_APPROACH = "enableNgramApproach";
    private static SpellCheckClient SPELL_CHECK_CLIENT = null;
    private MerchantNameExtractionUtil merchantNameExtractionUtil = MerchantNameExtractionUtil.getInstance();
    private Utils utils = Utils.getInstance();

    @Value("${spellcheck-server}")
    private String spellCheckUrl;


    @Override
    public void afterPropertiesSet() throws Exception {
        if (SPELL_CHECK_CLIENT == null){
            SPELL_CHECK_CLIENT = new SpellCheckClient(
                    spellCheckUrl );
        }
    }

    @Override
    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName ,Map<String,Object> config)
    {

        String ocrText = feRequest.getOcrData().getRawText();
        List<String> text = utils.getStringLines( ocrText );
        List<ExtractedValue> extractedValueList = new ArrayList<>();
        ExtractedValue extractedMerchantName;
        String merchantName = null;
        String matchedValue = null;
        Double ocrConfidence = 0.99;
        int textIndex = 0;
        Set<Entry<String, Tuple3<Double, String, Integer>>> merchantsExt = findMerchantFromText( text, ocrConfidence )
            .entrySet();
        Tuple3<Double, String, Integer> base = new Tuple3<>( 0.0, "", 0 );
        for ( Entry<String, Tuple3<Double, String, Integer>> merchantEntry : merchantsExt ) {
            Tuple3<Double, String, Integer> val = merchantEntry.getValue();
            if ( base.f1() < val.f1() ) {
                base = val;
                merchantName = merchantEntry.getKey();
                matchedValue = val.f2();
                textIndex = val.f3();
            }
        }
        double confidence = base.f1() > MAX_CONFIDENCE ? MAX_CONFIDENCE : base.f1();
        extractedMerchantName = new ExtractedValue( merchantName, MerchantNameExtractorType.EXISTING_MERCHANT, null, textIndex,
            confidence );
        extractedMerchantName.setMatchedValue( matchedValue );
        if ( extractedMerchantName.getValue() != null ) {
            extractedValueList.add( extractedMerchantName );
        }
        LOG.debug( "Obtained candidate merchant from text : {}", extractedMerchantName );
//        Object enableNgram = feRequest.getExtractionConfiguration( ENABLE_N_GRAM_APPROACH );
        boolean enableNgramApproach = true;
//        if ( enableNgram != null )
//            enableNgramApproach = (boolean) enableNgram;
        if ( enableNgramApproach ) {
            List<NGram> ngramResponse = findMerchantUsingNGram( text, ocrConfidence );
            for ( NGram ngram : ngramResponse ) {
                confidence = ngram.getLine() + ( ngram.getNGramSize() * 0.01 ) + ( ngram.getScore() * 0.075 );
                if ( confidence > MINIMUM_CONFIDENCE ) {
                    extractedMerchantName = new ExtractedValue( ngram.getNGramStr(),
                        MerchantNameExtractorType.EXISTING_MERCHANT, null, ngram.getLineIndex(), confidence );
                    extractedMerchantName.setMatchedValue( ngram.getMatchedVal() );
                    if ( !contains( extractedValueList, extractedMerchantName, ocrText ) )
                        extractedValueList.add( extractedMerchantName );
                }
            }
        }
        LOG.debug( "Obtained list of candidate values for existing merchantname: {}", extractedValueList );
        return extractedValueList;
    }


    /*
     * this method is to check if newVal is already contained in existing extracted list
     * in case line by line based approach have created a candidate value which is same as the one
     * being generated using ngram approach we will skip it
     */
    private boolean contains(List<ExtractedValue> extractedValues, ExtractedValue newVal, String text )
    {
        boolean response = false;
        Iterator<ExtractedValue> val = extractedValues.iterator();
        while ( val.hasNext() ) {
            ExtractedValue extVal = val.next();
            if ( extVal.getValue().equals( newVal.getValue() ) && extVal.getIndex() == newVal.getIndex() ) {
                response = true;
                break;
            }
        }
        return response;
    }


    public  List<String> getLinesforCandidates(List<String> text )
    {
        List<String> cleanedText = merchantNameExtractionUtil.filter( merchantNameExtractionUtil.clean( text ) );

        if ( !MerchantNameExtractionUtil.ENABLE_SEQUENTIAL_LOOKUP ) {
            cleanedText = merchantNameExtractionUtil.reorder( cleanedText );
        }
        cleanedText = cleanedText.subList( 0, Math.min( 7, cleanedText.size() ) );
        return cleanedText;
    }


    /**
     * Finds the merchant name in the given text
     *
     * @param text Text to find the merchant name in
     * @param ocrConfidence
     * @return Merchant name if found, null otherwise
     */
    private  Map<String, Tuple3<Double, String, Integer>> findMerchantFromText(final List<String> text,
                                                                               Double ocrConfidence )
    {
        double score = UtilConditions.SCORE_INITIAL;
        Map<String, Tuple3<Double, String, Integer>> matched = new HashMap<>();
        List<String> cleanedText = getLinesforCandidates( text );

        List<String> toSendToLookup = new ArrayList<>();
        Map<String, Integer> indexLookup = new HashMap<>();
        int i = 0, size = cleanedText.size();
        for ( String line : cleanedText ) {
            String cleanLine = merchantNameExtractionUtil.getCleanText( line );
            if ( cleanLine != null ) {
                toSendToLookup.add( cleanLine );

                if ( i % 2 == 0 ) {
                    indexLookup.put( cleanLine, i / 2 );
                } else {
                    indexLookup.put( cleanLine, ( size - 1 ) - ( i / 2 ) );
                }
            }
            i++;
        }

        Map<String, String> possibleMerchantNamesFromLookup = findPossibleMerchantNamesFromLookup( toSendToLookup,
            ocrConfidence );

        for ( String line : toSendToLookup ) {

            String merchant = possibleMerchantNamesFromLookup.get( line );
            if (  merchant !=null && !merchant.isEmpty() ) {
                if ( !matched.containsKey( merchant ) ) {
                    matched.put( merchant, new Tuple3( score, line, indexLookup.get( line ) ) );
                } else {
                    matched.get( merchant ).f1( matched.get( merchant ).f1() + score );
                }
            }
            score = score * UtilConditions.SCORE_DECAY;

        }
        return matched;
    }


    /**
     * Finds the merchant name in the given text using ngrams approach
     *
     * @param text          Text to find the merchant name in
     * @param ocrConfidence
     * @return Merchant name if found, empty list otherwise
     */
    private List<NGram> findMerchantUsingNGram(List<String> text, Double ocrConfidence )
    {
        LOG.debug( "Generating merchant candidates using Ngrams" );
        List<String> cleanedText = merchantNameExtractionUtil.filter( merchantNameExtractionUtil.clean( text ) );

        List<String[]> lines = merchantNameExtractionUtil.tokenizeText( cleanedText );

        List<NGram> ngrams = merchantNameExtractionUtil.getNGrams( lines, UtilConditions.MAX_NGRAM_SIZE, UtilConditions.MIN_NGRAM_SIZE );

        LOG.trace( "Got Ngrams : {}", ngrams );
        List<String> valuesToSend = getValueList( ngrams );

        Map<String, List<Map<String, Object>>> possibleMerchantNamesFromLookup = findPossibleNGramFromLookup( valuesToSend,
            ocrConfidence );

        LOG.trace( "Got Possible Merchants : {}", possibleMerchantNamesFromLookup );

        List<NGram> finalResponse = new ArrayList<>();
        for ( NGram ngram : ngrams ) {
            LOG.trace( "nGramVal: {}", ngram );
            List<Map<String, Object>> lookUpEntry = possibleMerchantNamesFromLookup.get( ngram.getNGramStr().trim() );
            if ( lookUpEntry != null && !lookUpEntry.isEmpty() ) {
                Map<String, Object> value = lookUpEntry.get( 0 );
                ngram.setNGramStr( value.get( "key" ).toString() );
                ngram.setNGramSize( value.get( "key" ).toString().split( "\\s+" ).length );
                ngram.setScore( Double.parseDouble( value.get( "score" ).toString() ) );
                finalResponse.add( ngram );
            }
        }

        // sorting using Collections.sort(list, comparator)
        Collections.sort( finalResponse, Collections.reverseOrder( new NGramComparator() ) );

        LOG.trace( "Merchant candidates using Ngrams : {}", finalResponse );
        return finalResponse;
    }


    public Map<String, List<Map<String, Object>>> findPossibleNGramFromLookup(List<String> lines, Double ocrConfidence )
    {
        LOG.trace( "Fetching merchant name suggestions for {}.", lines );
        if ( lines != null && !lines.isEmpty() ) {
            try {
                SpellCheckRequestEntity spellCheckRequestEntity = new SpellCheckRequestEntity( "merchants", lines,
                    ocrConfidence );
                return SPELL_CHECK_CLIENT.spellCheckAPI( spellCheckRequestEntity );
            } catch ( SpellCheckException e ) {
                LOG.error( "SpellCheckException while finding possible nGrams from lookup for {}", lines, e );
            }
        }
        //return empty hashmap
        return new HashMap<>();
    }


    private  Map<String, String> findPossibleMerchantNamesFromLookup(List<String> lines, Double ocrConfidence )
    {

        Map<String, String> result = new HashMap<>();
        Map<String, List<Map<String, Object>>> entries = findPossibleNGramFromLookup( lines, ocrConfidence );

        for ( String line : lines ) {
            List<Map<String, Object>> list = entries.get( line );
            if ( list != null && !list.isEmpty() ) {
                String key = list.get( 0 ).get( "key" ).toString();
                key = key.replaceAll( "^\"|\"$", "" );
                result.put( line, key );
            }
        }

        LOG.debug( "Returning {}", result );
        return result;
    }


    public List<String> getValueList(List<NGram> value )
    {
        List<String> response = new ArrayList<>();
        for ( NGram ngram : value ) {
            response.add( ngram.getNGramStr() );
        }
        return response;
    }

}