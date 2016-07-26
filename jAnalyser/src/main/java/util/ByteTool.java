package util;

/**
 * 字节的工具
 * Created by Sissel on 2016/7/25.
 */
public class ByteTool
{
    /**
     * 当作无符号数处理
     */
    public static int bigEnd(byte...bytes)
    {
        int length = bytes.length;
        assert length < 4;

        int sum = 0;
        for (byte B : bytes)
        {
            --length;
            if (B >= 0)
            {
                sum += B << (length * 8);
            }
            else
            {
                int s = (int)B + 256;
                sum += s << (length * 8);
            }
        }

        return sum;
    }

    public static int genFlags(byte head, byte tail)
    {
        return (head << 8) + tail;
    }
}
