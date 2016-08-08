package sissel.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 转换成每种类型的默认值
 * Created by Sissel on 2016/8/7.
 */
public class DescriptorHelper
{
    public static Object getDefault(String descriptor)
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

    private static int index = 0;
    public static List<String> split(String descriptor)
    {
        List<String> result = new LinkedList<>();
        char[] despChs = descriptor.toCharArray();
        index = 0;
        boolean makingReturn = false;

        while (index < despChs.length)
        {
            if (makingReturn)
            {
                result.add(new String(despChs, index, despChs.length - index));
                break;
            }
            else
            {
                switch (despChs[index])
                {
                    case 'L':
                        result.add(makeClass(despChs));
                        break;
                    case '(':
                        ++index;
                        break;
                    case ')':
                        ++index;
                        makingReturn = true;
                        break;
                    case '[':
                        result.add(makeArray(despChs));
                        break;
                    default:
                        result.add(new String(despChs, index, 1));
                        ++index;
                        break;
                }
            }
        }

        return result;
    }

    private static String makeClass(char[] chs)
    {
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            char ch = chs[index++];
            sb.append(ch);
            if (ch == ';')
            {
                break;
            }
        }

        return sb.toString();
    }

    private static String makeArray(char[] chs)
    {
        StringBuilder sb = new StringBuilder();
        while (true)
        {
            char ch = chs[index];
            if (ch == 'L')
            {
                sb.append(makeClass(chs));
                break;
            }
            else if (ch == '[')
            {
                sb.append(ch);
                ++index;
            }
            else
            {
                sb.append(ch);
                ++index;
                break;
            }
        }

        return sb.toString();
    }
}
