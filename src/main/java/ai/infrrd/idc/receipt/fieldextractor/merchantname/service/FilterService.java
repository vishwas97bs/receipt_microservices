package ai.infrrd.idc.receipt.fieldextractor.merchantname.service;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.filter.implementation.*;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FilterService {

    @Autowired
    RemoveJunkValues removeJunkValues;

    @Autowired
    private ConfidenceValueSortFilter confidenceValueSortFilter;

    @Autowired
    private CustomerKnownMerchantFilter customerKnownMerchantFilter;

    @Autowired
    private MerchantNameFinalizerFilter merchantNameFinalizerFilter;

    @Autowired
    private PunctuationsInMerchantNameFilter punctuationsInMerchantNameFilter;

    @Autowired
    private
    RegexExtractorCandidateConfidenceBoostingFilter regexExtractorCandidateConfidenceBoostingFilter;

    public  List<ExtractedValue> filterProcess(List<ExtractedValue> values, FieldExtractionRequest fieldExtractionRequest){
        List<ExtractedValue> filteredValue = regexExtractorCandidateConfidenceBoostingFilter.filter(values,fieldExtractionRequest);
        filteredValue = removeJunkValues.filter(filteredValue,fieldExtractionRequest);
        filteredValue = confidenceValueSortFilter.filter(filteredValue,fieldExtractionRequest);
        filteredValue = customerKnownMerchantFilter.filter(filteredValue,fieldExtractionRequest);
        filteredValue = merchantNameFinalizerFilter.filter(filteredValue,fieldExtractionRequest);
        filteredValue = punctuationsInMerchantNameFilter.filter(filteredValue,fieldExtractionRequest);

        return filteredValue;
    }
}
