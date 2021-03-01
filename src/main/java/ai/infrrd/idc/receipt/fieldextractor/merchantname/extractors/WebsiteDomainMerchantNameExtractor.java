package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.service.ConfigService;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.*;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.ConfidenceValueCollection;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantConstants;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class WebsiteDomainMerchantNameExtractor implements CandidateValueExtractor
{

    private static final String WEBSITE_DOMAIN_SEPERATOR = "\\.";
    private static final String WEBSITE_PARTS_SEPERATOR = "[-\\.]";
    private static final Logger LOG = LoggerFactory.getLogger( WebsiteDomainMerchantNameExtractor.class );
    private MerchantNameExtractionUtil merchantNameExtractionUtil;
    private Utils utils;
    private ConfigService configService;

    @Autowired
    public void setConfigService( ConfigService configService )
    {
        this.configService = configService;
    }


    @Autowired
    public void setMerchantNameExtractionUtil( MerchantNameExtractionUtil merchantNameExtractionUtil )
    {
        this.merchantNameExtractionUtil = merchantNameExtractionUtil;
    }


    @Autowired
    public void setUtils( Utils utils )
    {
        this.utils = utils;
    }


    /**
     * Extracts the website domain from a given text by identifying the first
     * website in the given text starting at the bottom.
     * <p>
     * Example:
     * <p>
     * Line One Line two has www.domain1.com in it Line three has
     * www.domain2.com in it
     * <p>
     * Extracted domain: domain2
     *
     * @param text
     *            Text to extract domain from
     * @return Domain name if found, null otherwise
     */


    private List<ExtractedValue> extractWebsiteDomain( List<String> text, FieldExtractionRequest data,
        Map<String, Object> config )
    {
        List<ExtractedValue> response = new ArrayList<>();
        List<String> reOrderedText = merchantNameExtractionUtil.reorder( text );
        List<String> regexList = configService.getRegexList( MerchantConstants.MERCHANT_NAME_WEBSITE, config, null );
        String superDomains = (String) configService
            .getExtractionConfiguration( MerchantConstants.MERCHANT_NAME_WEBSITE_DOMAINS, config );
        List<String> secondLevelDomains = configService
            .getValueList( MerchantConstants.MERCHANT_NAME_WEBSITE_SECOND_LEVEL_DOMAINS, config );
        superDomains = superDomains + "|" + superDomains.toUpperCase();
        double diff = ConfidenceValueCollection.WEBSITE_BASED_MERCHANT_EXTRACTOR
            - ConfidenceValueCollection.FIRST_LINE_BASED_MERCHANT_EXTRACTOR;
        double scoreDecay = ( 3 * diff / 2 ) / reOrderedText.size();
        double confidence = ConfidenceValueCollection.WEBSITE_BASED_MERCHANT_EXTRACTOR;
        for ( String str : reOrderedText ) {
            for ( String regex : regexList ) {
                regex = regex.replace( MerchantConstants.REGEX_POS_SUPER_DOMAIN, superDomains );
                PatternExtractor regexPattern = new PatternExtractor( regex );
                List<RegexMatchInfo> regexMatchInfoList = regexPattern.matchedPatterns( str );

                for ( RegexMatchInfo regexMatchInfo : regexMatchInfoList ) {
                    String value;
                    value = stripSuperDomain( new PatternExtractor( regex )
                        .matchedPatterns( regexMatchInfo.getMatchedString(), 1 ).get( 0 ).getMatchedString(), superDomains,
                        secondLevelDomains );
                    if ( value != null && merchantNameExtractionUtil.isPossibleWebsite( value ) ) {
                        double itemConfidence = MerchantNameExtractionUtil.getConfidence( value, confidence, 0 );
                        value = value.replaceAll( WEBSITE_PARTS_SEPERATOR, " " );
                        ExtractedValue extractedMerchantName = new ExtractedValue( value,
                            MerchantNameExtractorType.WEBSITE_DOMAIN, null, regexMatchInfo.getStartindex(), itemConfidence );
                        extractedMerchantName.setMatchedValue( new PatternExtractor( regex )
                            .matchedPatterns( regexMatchInfo.getMatchedString(), 1 ).get( 0 ).getMatchedString() );
                        response.add( extractedMerchantName );
                        LOG.debug( "Extracted Value : {}", extractedMerchantName );
                    }
                }
            }
            confidence = confidence - scoreDecay;
        }
        return response;
    }


    private static String stripSuperDomain( String value, String superDomains, List<String> secondLevelDomains )
    {
        String response = null;
        String[] parts = value.split( WEBSITE_DOMAIN_SEPERATOR );
        if ( parts.length > 2 ) {
            for ( String secondLevelDomain : secondLevelDomains ) {
                String[] domainParts = secondLevelDomain.split( WEBSITE_DOMAIN_SEPERATOR );
                if ( domainParts.length >= parts.length )
                    continue;
                StringBuilder val1 = new StringBuilder();
                StringBuilder val2 = new StringBuilder();
                for ( int i = 0; i < domainParts.length; i++ ) {
                    val1.append( domainParts[i] );
                    if ( parts[parts.length - 1 - i] != null )
                        val2.insert( 0, parts[parts.length - 1 - i] );
                }
                int distance = LevenstheinDistance.getLevenshteinDistance( val1.toString(), val2.toString(),
                    (int) Math.ceil( val2.length() * .3 ) );
                if ( distance < 0 )
                    continue;
                else {
                    int valueIndex = parts.length - domainParts.length - 1;
                    if ( valueIndex >= 0 ) {
                        response = parts[valueIndex];
                        break;
                    }
                }
            }
        }
        if ( response == null ) {
            for ( int i = parts.length - 1; i >= 0; i-- ) {
                String val = parts[i].trim();
                if ( val.matches( superDomains ) ) {
                    continue;
                } else {
                    response = val;
                    break;
                }
            }
        }
        return response;
    }


    @Override
    public List<ExtractedValue> extractValue( FieldExtractionRequest feRequest, String fieldName, Map<String, Object> config )
    {

        String ocrText = feRequest.getOcrData().getRawText();
        List<String> text = utils.getStringLines( ocrText );
        List<ExtractedValue> extractedValueList;
        // Remove obvious bad lines of text
        List<String> cleanLines = merchantNameExtractionUtil.filter( lightClean( text ) );
        LOG.trace( "Got cleaned Lines : {}", cleanLines );
        if ( cleanLines.size() == 0 ) {
            // Couldn't find any useful lines
            LOG.error( "No Text input {} ", feRequest.getRequestId() );
            return null;
        } else {
            // Try to extract a website domain from the text
            extractedValueList = extractWebsiteDomain( cleanLines, feRequest, config );

            for ( ExtractedValue value : extractedValueList ) {

                for ( ExtractedValue toCompare : extractedValueList ) {
                    if ( extractedValueList.indexOf( toCompare ) == extractedValueList.indexOf( value ) ) {
                        continue;
                    } else {
                        if ( ( (String) value.getValue() ).contains( (String) toCompare.getValue() )
                            && !value.getValue().equals( toCompare.getValue() ) ) {
                            value.setConfidence( value.getConfidence() - .1 );
                            break;
                        }
                    }
                }

            }

        }
        return extractedValueList;
    }


    private List<String> lightClean( List<String> originalList )
    {
        List<String> finalList = new ArrayList<>();
        for ( String str : originalList ) {
            String val = lightCleanUp( str );
            if ( val.length() > 0 )
                finalList.add( val );
        }
        return finalList;
    }


    private String lightCleanUp( String str )
    {
        return str.replaceAll( "[^\\w.\\-'’ü&:!@()]+", " " ).trim();
    }


}
