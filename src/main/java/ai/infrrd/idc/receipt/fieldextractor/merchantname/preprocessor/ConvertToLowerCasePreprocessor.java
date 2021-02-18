package ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.preprocessorinterface.TextPreprocessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertToLowerCasePreprocessor implements TextPreprocessor {

    private static final Logger LOG = LoggerFactory.getLogger( ConvertToLowerCasePreprocessor.class );


    @Override
    public String preProcessText(String input, FieldExtractionRequest fieldExtractionRequest, String fieldName )
    {
        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            // Convert to lower case
            preProcessedText = preProcessedText.toLowerCase();
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;
    }
}
