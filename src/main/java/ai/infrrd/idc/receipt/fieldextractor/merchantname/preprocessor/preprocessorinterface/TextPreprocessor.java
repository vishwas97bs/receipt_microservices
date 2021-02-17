package ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.preprocessorinterface;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;

public interface TextPreprocessor {
    public String preProcessText(String input, FieldExtractionRequest fieldExtractionRequest, String fieldName );

}
