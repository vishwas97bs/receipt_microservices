package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;

import java.util.List;
import java.util.Map;

public interface CandidateValueExtractor {
    List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName, Map<String, Object> config);
}
