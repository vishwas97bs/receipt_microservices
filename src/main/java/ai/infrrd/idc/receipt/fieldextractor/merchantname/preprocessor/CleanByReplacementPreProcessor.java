package ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor;

import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.preprocessorinterface.TextPreprocessor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CleanByReplacementPreProcessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( CleanByReplacementPreProcessor.class );


    @Override
    public String preProcessText(String input, FieldExtractionRequest feRequest, String fieldName )
    {

        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            // Remove or replace unwanted characters
            preProcessedText = cleanByReplacement( preProcessedText );
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;

    }


    /**
     * Clean by replacement.
     *
     * @param data
     *            the data
     * @return the string
     */
    private String cleanByReplacement(String data )
    {
        /*
         * TODO: Check whether replacement is done according to the requirement
         * of candidate value extractor
         */

        // replace *s to 's
        String text = data.replaceAll( "\\*s ", "'s " );
        text = text.replaceAll( "[ï»¿•„¦™«¼^ä]", "*" );

        /*Added based on output of previous preprocessors*/
        text = text.replaceAll( "(â‚¬)", "" );
        return text;
    }

}
