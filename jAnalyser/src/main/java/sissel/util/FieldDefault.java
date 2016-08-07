package sissel.util;

/**
 * 转换成每种类型的默认值
 * Created by Sissel on 2016/8/7.
 */
public class FieldDefault
{
    public static Object fromDescriptor(String descriptor)
    {
        switch (descriptor)
        {
            case "B": // byte
                return (byte)0;
            case "C": // char
                return '\u0000';
            case "D": // double
                return 0.0;
            case "F": // float
                return 0.0f;
            case "I":
                return 0;
            case "J":
                return 0L;
            case "S":
                return (short)0;
            case "Z":
                return false;
            default:
                return null;
        }
    }
}
