package sissel.test_cases;

/**
 * 加法指令
 * Created by Sissel on 2016/7/30.
 */
public class Add
{
    int v = 2;

    public int testInt()
    {
        int ia = 1, ib = 2, ic = Integer.MAX_VALUE;
        return ia + ib +ic;
    }

    public long testLong()
    {
        long a = 1L, b = 2L, c = Long.MAX_VALUE;
        return a + b + c;
    }

    public float testFloat()
    {
        float a = 1f, b = 2f, c = Float.MAX_VALUE;
        return a + b + c;
    }

    public double testDouble()
    {
        double a = 1d, b = 2d, c = Double.MAX_VALUE;
        return a + b + c;
    }
}
