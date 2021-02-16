package ai.infrrd.idc.receipt.fieldextractor.merchantname.exception;

public class InvalidConfigurationsException extends RuntimeException{
    private static final long serialVersionUID = 1L;


    public InvalidConfigurationsException( String message )
    {
        super( message );
    }


    public InvalidConfigurationsException( String message, Throwable cause )
    {
        super( message, cause );
    }

}
