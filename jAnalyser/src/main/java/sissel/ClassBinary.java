package sissel;

import util.ByteTool;
import util.File2ByteArray;

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * 解析二进制Class文件
 * Created by Sissel on 2016/7/25.
 */
public class ClassBinary
{
    private byte[] bytes;
    int minor_version;
    int major_version;

    short access_flags;

    int constant_pool_count;

    String this_class;
    String super_class;
    int interfaces_count;
    String[] interfaces;

    int fields_count;

    int methods_count;

    int attributes_count;

    private int analyzeConstPool()
    {
        int count = constant_pool_count - 1;
        int index = 10;
        while (count > 0)
        {
            --count;

            EConstPoolItem item = EConstPoolItem.getByTag(bytes[index]);
            if (item == EConstPoolItem.UTF8_INFO)
            {
                int length = ByteTool.bigEnd(bytes[index + 1], bytes[index + 2]);
                index += length + 3;
            }
            else
            {
                index += item.count;
            }

            // 这规定mdzz
            if (item == EConstPoolItem.DOUBLE_INFO || item == EConstPoolItem.LONG_INFO)
            {
                --count;
            }

            System.out.println(item.name());
        }

        return index;
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


        // this_class


        // super_class


        // interfaces
        int interfacesOffset = over_index + 6;
        interfaces_count = ByteTool.bigEnd(bytes[interfacesOffset], bytes[interfacesOffset + 1]);

        // fields
        int fieldsOffset = interfacesOffset + 2 + interfaces_count * 2;
        fields_count = ByteTool.bigEnd(bytes[fieldsOffset], bytes[fieldsOffset + 1]);

        // methods

        // attributes

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
            String path = ClassBinary.class.getResource("HelloWorld.class").getPath();
            ClassBinary clbin = new ClassBinary(path);
            System.out.println("over");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
