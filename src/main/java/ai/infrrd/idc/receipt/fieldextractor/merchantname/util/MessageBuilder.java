package ai.infrrd.idc.receipt.fieldextractor.merchantname.util;

import ai.infrrd.idc.receipt.fieldextractor.merchantname.config.ApplicationMessages;
import org.springframework.stereotype.Component;


@Component
public class MessageBuilder
{

    public String getMessage( String code )
    {

        String message = ApplicationMessages.getMessage( code );

        if ( message == null || message.isEmpty() ) {
            message = "default message";
        }
        return message;
    }

}
