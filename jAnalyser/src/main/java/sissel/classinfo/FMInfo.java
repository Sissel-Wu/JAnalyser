package sissel.classinfo;

/**
 * FieldInfo和MethodInfo的抽象父类
 * Created by Sissel on 2016/7/26.
 */
public abstract class FMInfo
{
    public ClassBinary classBinary;

    public int access_flags;
    public String name;
    public String descriptor;
    public int attributes_count;
    public AttributeInfo[] attributes;

    public FMInfo(ClassBinary classBinary, int access_flags, String name, String descriptor,
                  int attributes_count, AttributeInfo[] attributes)
    {
        this.classBinary = classBinary;
        this.access_flags = access_flags;
        this.name = name;
        this.descriptor = descriptor;
        this.attributes_count = attributes_count;
        this.attributes = attributes;
    }

    public int length()
    {
        int sum = 8;
        for (int i = 0; i < attributes_count; i++)
        {
            sum += attributes[i].length();
        }
        return sum;
    }

    public boolean isPublic()
    {
        return (access_flags & 0x0001) != 0;
    }

    public boolean isPrivate()
    {
        return (access_flags & 0x0002) != 0;
    }

    public boolean isProtected()
    {
        return (access_flags & 0x0004) != 0;
    }

    public boolean isStatic()
    {
        return (access_flags & 0x0008) != 0;
    }

    public boolean isFinal()
    {
        return (access_flags & 0x0010) != 0;
    }
}
