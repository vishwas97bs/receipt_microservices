package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;

import java.util.List;

public interface CandidateValueExtractor {

    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest , String fieldName );
}
