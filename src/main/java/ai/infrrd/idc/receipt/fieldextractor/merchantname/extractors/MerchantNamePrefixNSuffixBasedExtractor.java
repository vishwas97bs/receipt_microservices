//package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;
//
//
//import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.PatternExtractor;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.RegexMatchInfo;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.ConfidenceValueCollection;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantConstants;
//import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.MerchantNameExtractorType;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.regex.PatternSyntaxException;
//
//
//public class MerchantNamePrefixNSuffixBasedExtractor implements CandidateValueExtractor
//{
//
//    private static final Logger LOG = LoggerFactory.getLogger( MerchantNamePrefixNSuffixBasedExtractor.class );
//
//
//    @Override
//    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName )
//    {
//
//        LOG.debug( "Entering extract Method of Regex extraction with fieldName: {}", fieldName );
//
//        if ( feRequest.getOcrData().getRawText().isEmpty() ||  fieldName.isEmpty() ) {
//            LOG.error( "input text or field name can't be null or empty." );
//            // TODO to throw a custom exception
//            return Collections.emptyList();
//        }
//        String inputText = preProcessText( feRequest.getOcrData().getRawText(), feRequest, fieldName );
//
//        List<String> tagLines = getTagLines( feRequest );
//        List<String> suffixLines = getSuffixLines( feRequest );
//        List<String> tagLinesRegexList = feRequest.getAllValueList( "merchant_name_tag_lines", "_extract_regex" );
//        List<String> suffixRegexList = feRequest.getAllValueList( "merchant_name_suffix", "_extract_regex" );
//
//        double baseConfidence = ConfidenceValueCollection.TAGLINE_BASED_MERCHANT_EXTRACTOR;
//        List<ExtractedValue> response = new ArrayList<>();
//
//        List<String> matchedTagLines = removeSubParts( getMatchedLines( tagLines, inputText ) );
//        LOG.debug( "Tag lines matched for field{}:\n{}", fieldName, matchedTagLines );
//        response.addAll(
//            extractMatchedPatterns( fieldName, inputText, matchedTagLines, tagLinesRegexList, MerchantConstants.REGEX_POS_TAG_LINE,
//                MerchantNameExtractorType.TAG_LINE, baseConfidence ) );
//
//        List<String> matchedSuffixLines = removeSubParts( getMatchedLines( suffixLines, inputText ) );
//        LOG.debug( "Suffix lines matched for field{}:\n{}", fieldName, matchedSuffixLines );
//        response.addAll(
//            extractMatchedPatterns( fieldName, inputText, matchedSuffixLines, suffixRegexList, MerchantConstants.REGEX_POS_SUFFIX_LINE,
//                MerchantNameExtractorType.SUFFIX_LINE, baseConfidence ) );
//
//        LOG.debug( "Matched text: {}", response );
//        return response;
//    }
//
//
//    private List<ExtractedValue> extractMatchedPatterns(String fieldName, String inputText, List<String> subPatterns,
//                                                        List<String> regexList, String replacePattern, String operation, double confidence )
//    {
//        List<ExtractedValue> response = new ArrayList<>();
//        if ( !subPatterns.isEmpty() ) {
//            for ( String pattern : regexList ) {
//                for ( String suffix : subPatterns ) {
//                    String vicinityPattern = pattern.replace( replacePattern, suffix ).trim();
//                    Map<Integer, String> matchedWordMap = matchPattern( vicinityPattern, inputText );
//                    LOG.trace( "matchedWordMap for {}:\n{}", fieldName, matchedWordMap );
//                    response.addAll( matchMapToExtractedValueList( matchedWordMap, operation, suffix, confidence ) );
//                }
//            }
//        }
//        return response;
//    }
//
//
//    private List<ExtractedValue> matchMapToExtractedValueList(Map<Integer, String> matchedWordList, String operation,
//                                                              String matchedVicinity, Double confidence )
//    {
//        List<ExtractedValue> response = new ArrayList<>();
//        if ( !matchedWordList.isEmpty() ) {
//            for ( Map.Entry<Integer, String> matchedWord : matchedWordList.entrySet() ) {
//                ExtractedValue patternMatchInfo = new ExtractedValue( matchedWord.getValue().contains( matchedVicinity ) ?
//                    matchedWord.getValue().replace( matchedVicinity, "" ).trim() :
//                    matchedWord.getValue(), operation, matchedVicinity, matchedWord.getKey(), confidence );
//                LOG.trace( "FieldDetails with vicinity: {}", patternMatchInfo );
//                response.add( patternMatchInfo );
//            }
//        }
//        return response;
//    }
//
//
//    private List<String> getMatchedLines(List<String> tagLines, String inputText )
//    {
//        List<String> matchedTagLines = new ArrayList<>();
//        // Loop through vicinity words and add matching words to a list
//        for ( String tagLine : tagLines ) {
//            if ( matchPattern( tagLine, inputText ).size() > 0 ) {
//                matchedTagLines.add( tagLine );
//            }
//        }
//        return matchedTagLines;
//    }
//
//
//    private List<String> getSuffixLines(FieldExtractionRequest feRequest )
//    {
//        // TODO: need to move key to gimlet config keys (new collection)
//        List<String> suffixLines = feRequest.getValueList( "merchant_name_suffix" );
//        if ( feRequest.getLocale() != null ) {
//            List<String> localeBasedSuffixLines = feRequest.getValueList( feRequest.getLocale() + "_merchant_name_suffix" );
//            if ( localeBasedSuffixLines != null ) {
//                suffixLines.addAll( localeBasedSuffixLines );
//            }
//        }
//        return suffixLines;
//    }
//
//
//    private List<String> getTagLines(FieldExtractionRequest feRequest )
//    {
//        // TODO: need to move key to gimlet config keys (new collection)
//        List<String> tagLines = feRequest.getValueList( "merchant_name_tag_lines" );
//        if ( feRequest.getLocale() != null ) {
//            List<String> localeBasedTagLines = feRequest.getValueList( feRequest.getLocale() + "_merchant_name_tag_lines" );
//            if ( localeBasedTagLines != null ) {
//                tagLines.addAll( localeBasedTagLines );
//            }
//        }
//        return tagLines;
//    }
//
//
//    private String preProcessText(String inputText, FieldExtractionRequest feRequest, String fieldName )
//    {
//        // NEED to Process text locally since this pre processing may not be
//        // beneficial for other candidate value extractor start
//        String preProcessedText = inputText;
//        TextPreprocessor preProcessor = new ConvertToLowerCasePreprocessor();
//        preProcessedText = preProcessor.preProcessText( preProcessedText, feRequest, fieldName );
//        preProcessor = new RemoveExtraSpacesPreProcessor();
//        preProcessedText = preProcessor.preProcessText( preProcessedText, feRequest, fieldName );
//        preProcessor = new RemoveEmptyLinesPreprocessor();
//        return preProcessor.preProcessText( preProcessedText, feRequest, fieldName );
//    }
//
//
//    private List<String> removeSubParts(List<String> input )
//    {
//        List<String> response = new ArrayList<>();
//        for ( String val : input ) {
//            boolean subPart = false;
//            for ( String secVal : input ) {
//                if ( input.indexOf( val ) != input.indexOf( secVal ) && secVal.contains( val ) ) {
//                    subPart = true;
//                    break;
//                }
//            }
//            if ( !subPart ) {
//                response.add( val );
//            }
//        }
//        return response;
//    }
//
//
//    private Map<Integer, String> matchPattern(String patternString, String inputText )
//    {
//        String matchFound;
//        Map<Integer, String> patternList = new HashMap<>();
//        try {
//            PatternExtractor pattern = new PatternExtractor( patternString );
//            List<RegexMatchInfo> regexMatchInfoList = pattern.matchedPatterns( inputText );
//            for ( RegexMatchInfo regexMatchInfo : regexMatchInfoList ) {
//                matchFound = regexMatchInfo.getMatchedString();
//                int index = regexMatchInfo.getStartindex();
//                patternList.put( index, matchFound.trim() );
//                LOG.trace( "Found ({}) for pattern {} @ index {}", matchFound, patternString, index );
//            }
//        } catch ( PatternSyntaxException | IllegalStateException | IndexOutOfBoundsException e ) {
//            LOG.error( "Exception while trying to match pattern {} in {}", patternString, inputText, e );
//        }
//        return patternList;
//    }
//}
