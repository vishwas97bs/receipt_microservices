package ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor;


import ai.infrrd.idc.commons.entities.FieldExtractionRequest;
import ai.infrrd.idc.receipt.fieldextractor.merchantname.preprocessor.preprocessorinterface.TextPreprocessor;
import org.springframework.stereotype.Component;

@Component
public class RemoveStartEndJunkTextPreprocessor implements TextPreprocessor
{

    @Override
    public String preProcessText(String input, FieldExtractionRequest fieldExtractionRequest, String fieldName )
    {
        String response = "";
        String lines[] = input.split( "[\r\n]" );
        if ( lines.length > 0 ) {
            String firstLine = lines[0];
            String firstLineReplacement = getReplacement( firstLine );
            input = input.replace( firstLine, firstLineReplacement );
            String lastLine = lines[lines.length - 1];
            String lastLineReplacement = getReplacement( lastLine );
            input = input.replace( lastLine, lastLineReplacement );
            response = input.trim();
        } else {
            response = input;
        }

        return response;
    }


    private String getReplacement(String line )
    {
        return line.replace( "ï»¿", "" );// To remove Byte Order Mark (ï»¿)
    }
}
