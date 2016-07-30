package sissel.classinfo;

import util.ByteTool;
import util.File2ByteArray;
import util.var;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 解析二进制Class文件
 * Created by Sissel on 2016/7/25.
 */
public class ClassBinary
{
    private byte[] bytes;
    private int[] cp_index; // 输入常量池中的索引号，得到在bytes中对应的位置

    int minor_version;
    int major_version;

    int access_flags;

    int constant_pool_count;

    String this_class;
    String super_class;
    int interfaces_count;
    String[] interfaces;

    int fields_count;
    FieldInfo[] fields;

    int methods_count;
    MethodInfo[] methods;

    int attributes_count;
    AttributeInfo[] attributes;

    private int analyzeConstPool()
    {
        cp_index = new int[constant_pool_count];
        cp_index[0] = -1;

        int index = 1;
        int offset = 10;
        while (index < constant_pool_count)
        {
            cp_index[index] = offset;
            ++index;

            EConstPoolItem item = EConstPoolItem.getByTag(bytes[offset]);
            if (item == EConstPoolItem.UTF8_INFO)
            {
                int length = ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2]);
                offset += length + 3;
            }
            else
            {
                offset += item.count;
            }

            // 这规定mdzz
            if (item == EConstPoolItem.DOUBLE_INFO || item == EConstPoolItem.LONG_INFO)
            {
                ++index;
            }

            System.out.println(item.name());
        }

        return offset;
    }

    /**
     *
     * @param attributesOffset attributes部分的起始（包括count）在bytes中的位置
     * @param attributes 准备好的容器数组
     * @return 下一部分的开始的offset
     */
    private int analyzeAttributes(int attributesOffset, AttributeInfo[] attributes)
    {
        return AttributeInfo.analyzeHelp(this, bytes, attributesOffset, attributes);
    }

    private void analyzeInterfaces(int interfacesOffset)
    {
        interfaces_count = ByteTool.bigEnd(bytes[interfacesOffset], bytes[interfacesOffset + 1]);
        interfaces = new String[interfaces_count];
        for (int i = 0; i < interfaces_count; i++)
        {
            int index = ByteTool.bigEnd(bytes[interfacesOffset + i + 2], bytes[interfacesOffset + i + 3]);
            interfaces[i] = extractStrFromClassInfo(index);
        }
    }

    private int analyzeFields(int fieldsOffset)
    {
        fields_count = ByteTool.bigEnd(bytes[fieldsOffset], bytes[fieldsOffset + 1]);
        fields = new FieldInfo[fields_count];

        int current = fieldsOffset + 2;
        for (int i = 0; i < fields_count; i++)
        {
            int access_flags = ByteTool.genFlags(bytes[current], bytes[current + 1]);
            String name = extractString(ByteTool.bigEnd(bytes[current + 2], bytes[current + 3]));
            String descriptor = extractString(ByteTool.bigEnd(bytes[current + 4], bytes[current + 5]));

            int attributes_count = ByteTool.bigEnd(bytes[current + 6], bytes[current + 7]);
            AttributeInfo[] attributes = new AttributeInfo[attributes_count];

            current = analyzeAttributes(current + 6, attributes);
            FieldInfo fieldInfo = new FieldInfo(this, access_flags, name, descriptor, attributes_count, attributes);
            fields[i] = fieldInfo;
        }

        return current;
    }

    private int analyzeMethods(int methodsOffset)
    {
        methods_count = ByteTool.bigEnd(bytes[methodsOffset], bytes[methodsOffset + 1]);
        methods = new MethodInfo[methods_count];

        int current = methodsOffset + 2;
        for (int i = 0; i < methods_count; i++)
        {
            int access_flags = ByteTool.genFlags(bytes[current], bytes[current + 1]);
            String name = extractString(ByteTool.bigEnd(bytes[current + 2], bytes[current + 3]));
            String descriptor = extractString(ByteTool.bigEnd(bytes[current + 4], bytes[current + 5]));

            int attributes_count = ByteTool.bigEnd(bytes[current + 6], bytes[current + 7]);
            AttributeInfo[] attributes = new AttributeInfo[attributes_count];

            current = analyzeAttributes(current + 6, attributes); // 先解析attributes，否则方法构造会失败
            MethodInfo methodInfo = new MethodInfo(this, access_flags, name, descriptor, attributes_count, attributes);
            methods[i] = methodInfo;
        }

        return current;
    }

    /**
     * 验证cafebabe
     * 找出版本
     * 本类和父类
     * 分发field、method、interface、attribute的分析
     */
    private void analyze()
    {
        // check magic number ca=-54 fe=-2 ba=-70 be=-66
        if (bytes[0] != -54 || bytes[1] != -2 || bytes[2] != -70 || bytes[3] != -66) throw new ClassFormatError();

        // versions
        minor_version = ByteTool.bigEnd(bytes[4], bytes[5]);
        major_version = ByteTool.bigEnd(bytes[6], bytes[7]);

        // constant pool count
        constant_pool_count = ByteTool.bigEnd(bytes[8], bytes[9]);
        int over_index = analyzeConstPool();

        // access flags
        access_flags = ByteTool.genFlags(bytes[over_index], bytes[over_index + 1]);

        // this_class
        int this_class_index = ByteTool.bigEnd(bytes[over_index + 2], bytes[over_index + 3]);
        this_class = extractStrFromClassInfo(this_class_index);

        // super_class
        int super_class_index = ByteTool.bigEnd(bytes[over_index + 4], bytes[over_index + 5]);
        super_class = extractStrFromClassInfo(super_class_index);

        // interfaces
        int interfacesOffset = over_index + 6;
        analyzeInterfaces(interfacesOffset);

        // fields
        int fieldsOffset = interfacesOffset + 2 + interfaces_count * 2;
        int methodsOffset = analyzeFields(fieldsOffset);

        // methods
        int attributesOffset = analyzeMethods(methodsOffset);

        // attributes
        attributes_count = ByteTool.bigEnd(bytes[attributesOffset], bytes[attributesOffset + 1]);
        attributes = new AttributeInfo[attributes_count];
        analyzeAttributes(attributesOffset, attributes);
    }

    public String extractStrFromClassInfo(int cl_index)
    {
        int offset = cp_index[cl_index];
        assert bytes[offset] == 7;
        int utf8_index = ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2]);

        return extractString(utf8_index);
    }

    public String extractString(int index)
    {
        int offset = cp_index[index];
        assert bytes[offset] == 1;
        int length = ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2]);

        byte[] utf8Bytes = new byte[length];
        System.arraycopy(bytes, offset + 3, utf8Bytes, 0, length);

        try
        {
            return new String(utf8Bytes, "utf8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public ClassBinary(String filePath) throws IOException
    {
        this.bytes = File2ByteArray.read(filePath);
        this.analyze();
    }

    public static void main(String[] args)
    {
        try
        {
            String path = ClassBinary.class.getResource("../HelloWorld.class").getPath();
            ClassBinary clbin = new ClassBinary(path);
            System.out.println("over");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
