package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation.RegexExtractorCandidateConfidenceBoostingFilter;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreprocessorService {
    @Autowired
    CleanByReplacementPreProcessor cleanByReplacementPreProcessor;

    @Autowired
    RemoveExtraSpacesPreProcessor removeExtraSpacesPreProcessor;

    @Autowired
    private RemoveLineItemsPreProcessor removeLineItemsPreProcessor;

    @Autowired
    private RemoveStartEndJunkTextPreprocessor removeStartEndJunkTextPreprocessor;

    @Autowired
    private ReplaceSpecialCharacterPreProcessor replaceSpecialCharacterPreProcessor;

    public String preprocess(FieldExtractionRequest fieldExtractionRequest, String fieldName){
        StringBuilder preprocessedtext = new StringBuilder(removeLineItemsPreProcessor.preProcessText(fieldExtractionRequest.getOcrData().getRawText(),fieldExtractionRequest,fieldName));
        preprocessedtext =  new StringBuilder(removeExtraSpacesPreProcessor.preProcessText(preprocessedtext.toString(),fieldExtractionRequest,fieldName));
        preprocessedtext =  new StringBuilder(removeStartEndJunkTextPreprocessor.preProcessText(preprocessedtext.toString(),fieldExtractionRequest,fieldName));
        preprocessedtext =  new StringBuilder(replaceSpecialCharacterPreProcessor.preProcessText(preprocessedtext.toString(),fieldExtractionRequest,fieldName));
        preprocessedtext =  new StringBuilder(cleanByReplacementPreProcessor.preProcessText(preprocessedtext.toString(),fieldExtractionRequest,fieldName));
        return new String(preprocessedtext);
    }

}
