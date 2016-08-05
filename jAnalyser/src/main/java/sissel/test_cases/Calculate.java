package sissel.test_cases;

/**
 * 四则运算
 * Created by Sissel on 2016/8/4.
 */
public class Calculate
{
    public int testSub()
    {
        int a = Integer.MAX_VALUE, b = 1;
        return a - b;
    }

    public int testMul()
    {
        int a = 200, b = 3;
        return a * b;
    }

    public int testDiv()
    {
        int a = 100, b = 33;
        return a / b;
    }

    public int testMod()
    {
        int a = 100, b = 33;
        return a % b;
    }

    public int testNeg()
    {
        int a, b = Integer.MIN_VALUE, c = -1;
        a = -b;
        c = -c;
        return a + c;
    }

    public int testSH()
    {
        int a = 2;
        a = a << 10;
        a = a >> 9;
        a = -1;
        a = a >>> 3;

        return a;
    }

    public int testInc()
    {
        int a = 12;
        a += 10;
        return a;
    }

    public int testOther()
    {
        int a = 2, b = 3;
        a = ~a;
        a = a | b;
        a = a & b;
        a = a ^ b;
        return a;
    }
}
