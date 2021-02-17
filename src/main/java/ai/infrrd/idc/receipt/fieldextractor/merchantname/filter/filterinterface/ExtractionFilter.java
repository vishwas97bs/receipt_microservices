package ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.filterinterface;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;

import java.util.List;

public interface ExtractionFilter {
    public List<ExtractedValue> filter(List<ExtractedValue> input, FieldExtractionRequest fieldExtractionRequest);

}
