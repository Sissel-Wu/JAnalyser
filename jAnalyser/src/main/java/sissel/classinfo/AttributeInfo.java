package sissel.classinfo;

/**
 * 属性的信息
 * Created by Sissel on 2016/7/26.
 */
public class AttributeInfo
{
    String name;
    int length;
    byte[] info;

    public AttributeInfo(String name, int length, byte[] allBytes, int offset)
    {
        this.name = name;
        this.length = length;
        this.info = new byte[length];

        System.arraycopy(allBytes, offset, info, 0, length);
    }

    public int length()
    {
        return 6 + length;
    }
}
