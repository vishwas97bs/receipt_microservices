package ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.preprocessorinterface.TextPreprocessor;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReplaceSpecialCharacterPreProcessor implements TextPreprocessor
{

    private static final Logger LOG = LoggerFactory.getLogger( ReplaceSpecialCharacterPreProcessor.class );


    @Override
    public String preProcessText(String input, FieldExtractionRequest fieldExtractionRequest, String fieldName )
    {

        String preProcessedText = input;

        if ( !StringUtils.isEmpty( input ) ) {
            LOG.trace( "Text before preprocessing: {}", preProcessedText );
            // Unescape and replace the special characters
            preProcessedText = unescapeAndReplace( preProcessedText );
            LOG.trace( "Text after preprocessing: {}", preProcessedText );
        }

        return preProcessedText;

    }


    /**
     * Un-escape the special characters.
     *
     * @param text
     *            the data to be un-escaped.
     * @return the string with un-escaped text.
     */
    private String unescapeAndReplace(String text )
    {
        String data = text;
        try {
            data = replaceSpecialChar( 8217, data, "'" );
            data = replaceSpecialChar( 8216, data, "'" );
            data = replaceSpecialChar( 167, data, "&" );
            data = StringEscapeUtils.unescapeXml( data );
            data = data.replace( "&apos;", "'" );
            data = data.replaceAll( "[�]", "*" );
        } catch ( Exception e ) {
            LOG.error( "Error in unescaping string - \n" + data, e );
        }
        return data;
    }


    /**
     * Replace special char.
     *
     * @param hexToInt
     *            the hex to int
     * @param textVal
     *            the text val
     * @param replaceTo
     *            the replace to
     * @return the string
     */
    private String replaceSpecialChar(int hexToInt, String textVal, String replaceTo )
    {

        String textLocal = textVal;
        char intToChar = (char) hexToInt;
        textLocal = textLocal.replaceAll( intToChar + "", replaceTo );
        return textLocal;
    }
}
