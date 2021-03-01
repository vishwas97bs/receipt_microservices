package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common;


import ai.infrrd.idc.utils.exception.PatternMatchInterruptedException;

public class InterruptibleCharSequence implements CharSequence
{

    private CharSequence charSequence;


    public InterruptibleCharSequence( CharSequence charSequence )
    {
        this.charSequence = charSequence;
    }


    @Override
    public int length()
    {
        return charSequence.length();
    }


    @Override
    public char charAt(int index )
    {
        if ( Thread.currentThread().isInterrupted() ) {
            throw new PatternMatchInterruptedException( "Pattern match interrupted." );
        }
        return charSequence.charAt( index );
    }


    @Override
    public CharSequence subSequence(int start, int end )
    {
        return new InterruptibleCharSequence( charSequence.subSequence( start, end ) );
    }


    @Override
    public String toString()
    {
        return charSequence.toString();
    }
}
