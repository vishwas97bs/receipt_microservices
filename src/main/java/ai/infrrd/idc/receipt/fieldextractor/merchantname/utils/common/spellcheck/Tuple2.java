package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

/**
 * Defines a data tuple with two field values
 *
 * @author Sriram
 * @since 11/16/2016
 */
public class Tuple2<T1, T2>
{
    private T1 f1;
    private T2 f2;


    public Tuple2( T1 f1, T2 f2 )
    {
        this.f1( f1 );
        this.f2( f2 );
    }


    @Override
    public String toString()
    {
        return "(" + f1() + "," + f2() + ")";
    }


    public T1 f1()
    {
        return f1;
    }


    public void f1( T1 f1 )
    {
        this.f1 = f1;
    }


    public T2 f2()
    {
        return f2;
    }


    public void f2( T2 f2 )
    {
        this.f2 = f2;
    }
}
