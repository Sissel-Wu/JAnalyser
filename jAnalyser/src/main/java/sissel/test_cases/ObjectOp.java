package sissel.test_cases;

/**
 * 对象有关的指令
 * Created by Sissel on 2016/8/7.
 */
public class ObjectOp
{
    // 不用处理复杂的初始化方法的
    public void newSimple()
    {
        Simple s = new Simple();
    }

    public void newComplex()
    {
        Complex c = new Complex(12);
    }

    public void newSuperComplex()
    {
        Complex c = new SuperComplex(12);
    }
}

class Simple
{
    int i;
}

class Complex
{
    int i;

    Complex(int i)
    {
        this.i = i;
    }
}

class SuperComplex extends Complex
{
    int ii;

    SuperComplex(int i)
    {
        super(i);

        ii = i * 2;
    }
}