package sissel.classinfo;

import util.ByteTool;

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

    public static int analyzeHelp(ClassBinary classBinary, byte[] bytes, int beginOffset, AttributeInfo[] results)
    {
        int attributesCount = ByteTool.bigEnd(bytes[beginOffset], bytes[beginOffset + 1]);

        int current = beginOffset + 2;
        for (int i = 0; i < attributesCount; i++)
        {
            int attrIndex = ByteTool.bigEnd(bytes[current], bytes[current + 1]);
            String name = classBinary.extractString(attrIndex);
            int length = ByteTool.bigEnd(bytes[current + 2], bytes[current + 3], bytes[current + 4], bytes[current + 5]);

            AttributeInfo attributeInfo = new AttributeInfo(name, length, bytes, current + 6);
            results[i] = attributeInfo;

            current += 6 + length;
        }

        return current;
    }

    public int length()
    {
        return 6 + length;
    }
}
