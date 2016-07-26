package sissel.classinfo;

/**
 * FieldInfo和MethodInfo的抽象父类
 * Created by Sissel on 2016/7/26.
 */
public abstract class FMInfo
{
    int access_flags;
    String name;
    String descriptor;
    int attributes_count;
    AttributeInfo[] attributes;

    public int length()
    {
        int sum = 8;
        for (int i = 0; i < attributes_count; i++)
        {
            sum += attributes[i].length();
        }
        return sum;
    }
}
