package sissel.test_cases;

/**
 * 类型转换
 * Created by Sissel on 2016/8/5.
 */
public class TypeTrans
{
    public void i2x()
    {
        int origin = 128 + 32768 + 65536;

        char c = (char)origin;
        float f = (float) origin;
        double d = (double)origin;
        long l = (long)origin;
        byte b = (byte)origin;
        short s = (short)origin;
    }

    public void l2x()
    {
        long l = (long)Integer.MAX_VALUE + 1L;

        int i = (int)l;
        float f = (float)l;
        double d = (double)l;
    }

    public void f2x()
    {
        float f = Float.MAX_VALUE;

        int i = (int)f;
        long l = (long)f;
        double d = (double)f;
    }

    public void d2x()
    {
        double d = Double.MAX_VALUE;

        int i = (int)d;
        long l = (long)d;
        float f = (float)d;
    }
}
