package ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.extractors.inteface.CandidateValueExtractor;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.ExtractedValue;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.constants.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomerKnownMerchantExtractor implements CandidateValueExtractor {
    private static final Logger LOG = LoggerFactory.getLogger( CustomerKnownMerchantExtractor.class );

    @Override
    public List<ExtractedValue> extractValue(FieldExtractionRequest feRequest, String fieldName,Map<String,Object> config) {
        return findMerchantName(feRequest);
    }

    private List<ExtractedValue> findMerchantName(FieldExtractionRequest feRequest )
    {

        //commented as of now.
//        if(feRequest.) {
//            LOG.info("Entered find Merchant name");
//            List<String> customerKnownMerchants = new ArrayList<>();
//            customerKnownMerchants.add("COSTCO");
//            customerKnownMerchants.add("target");
//
//            List extractedValues = customerKnownMerchants.stream().
//                    filter(value -> {
//                        return feRequest.getOcrData().getRawText().toLowerCase().replaceAll(" ", "").contains(value.toLowerCase());
//                    }).
//                    map(value -> {
//                        ExtractedValue extractedValue = new ExtractedValue();
//                        extractedValue.setConfidence(1.0);
//                        extractedValue.setValue(value);
//                        extractedValue.setOperation(FieldMatch.CUSTOMER_KNOWN_MERCHANT.toString());
//                        return extractedValue;
//                    }).collect(Collectors.toList());
//
//            return extractedValues;
//        }
        return new ArrayList<>();
    }

}
