package ai.infrrd.idc.receipt.fieldextractor.merchantname.exception;

public class SpellCheckException extends Exception
{

    public SpellCheckException( String message )
    {
        super( message );
    }


    public SpellCheckException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
