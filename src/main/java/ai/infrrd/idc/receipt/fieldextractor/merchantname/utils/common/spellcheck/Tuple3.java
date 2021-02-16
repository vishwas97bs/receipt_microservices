package ai.infrrd.idc.receipt.fieldextractor.merchantname.utils.common.spellcheck;

/**
 * Defines a data tuple with three field values
 *
 * @author Sriram
 * @since 11/16/2016
 */
public class Tuple3<T1, T2, T3>
{
    private T1 f1;
    private T2 f2;
    private T3 f3;


    public Tuple3( T1 f1, T2 f2, T3 f3 )
    {
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
    }


    @Override
    public String toString()
    {
        return "(" + f1() + "," + f2() + "," + f3() + ")";
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


    public T3 f3()
    {
        return f3;
    }


    public void f3( T3 f3 )
    {
        this.f3 = f3;
    }
}
