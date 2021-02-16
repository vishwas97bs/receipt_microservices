package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils;

public class SpellCheckException extends Exception{
    public SpellCheckException( String message )
    {
        super( message );
    }


    public SpellCheckException( String message, Throwable cause )
    {
        super( message, cause );
    }
}
