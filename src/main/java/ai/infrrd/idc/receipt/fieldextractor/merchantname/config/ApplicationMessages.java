package ai.infrrd.idc.receipt.fieldextractor.merchantname.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;


@Configuration
@PropertySource( value = "classpath:codes.properties", name = "codes.props")
public class ApplicationMessages
{

    @Autowired
    Environment env;

    private static Map<String, String> codesAndMessages;

    private static final Logger logger = LoggerFactory.getLogger( ApplicationMessages.class );

    private static void setCodesAndMessages( Map<String, String> codesAndMessages )
    {
        ApplicationMessages.codesAndMessages = codesAndMessages;
    }


    public static String getMessage( String code )
    {
        return codesAndMessages.get( code );
    }


    @PostConstruct
    public void addCodes()
    {
        codesAndMessages = new HashMap<>();
        codesAndMessages.put( "FE-2000", "Successfully value extracted" );
        codesAndMessages.put( "FE-4004", "Field value not found" );
    }

}