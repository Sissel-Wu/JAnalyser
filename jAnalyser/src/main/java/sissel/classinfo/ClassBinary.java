package sissel.classinfo;

import javafx.util.Pair;
import sissel.util.ByteArrayTool;
import sissel.util.ByteTool;
import sissel.vm.HeapDump;
import sissel.vm.MyStackFrame;
import sissel.vm.ThreadCopy;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析二进制Class文件
 * Created by Sissel on 2016/7/25.
 */
public class ClassBinary implements IClass
{
    private byte[] bytes;
    private int[] cp_index; // 输入常量池中的索引号，得到在bytes中对应的位置

    int minor_version;
    int major_version;

    int access_flags;

    int constant_pool_count;

    String this_class;
    public String getThis_class()
    {
        return this_class;
    }

    String super_class;
    public String getSuper_class()
    {
        return super_class;
    }

    int interfaces_count;
    String[] interfaces;

    int fields_count;
    public FieldInfo[] fields;

    int methods_count;
    MethodInfo[] methods;

    int attributes_count;
    AttributeInfo[] attributes;

    public boolean initialized = false;
    public boolean prepared = false;
    private Map<Pair<String, String>, MethodInfo> methodMap; // key is <methodName, descriptor>, value is methodInfo

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
                int length = ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]);
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
        interfaces_count = ByteTool.uBigEnd(bytes[interfacesOffset], bytes[interfacesOffset + 1]);
        interfaces = new String[interfaces_count];
        for (int i = 0; i < interfaces_count; i++)
        {
            int index = ByteTool.uBigEnd(bytes[interfacesOffset + (i << 1) + 2], bytes[interfacesOffset + (i << 1) + 3]);
            interfaces[i] = extractStrFromClassInfo(index);
        }
    }

    private int analyzeFields(int fieldsOffset)
    {
        fields_count = ByteTool.uBigEnd(bytes[fieldsOffset], bytes[fieldsOffset + 1]);
        fields = new FieldInfo[fields_count];

        int current = fieldsOffset + 2;
        for (int i = 0; i < fields_count; i++)
        {
            int access_flags = ByteTool.genFlags(bytes[current], bytes[current + 1]);
            String name = extractString(ByteTool.uBigEnd(bytes[current + 2], bytes[current + 3]));
            String descriptor = extractString(ByteTool.uBigEnd(bytes[current + 4], bytes[current + 5]));

            int attributes_count = ByteTool.uBigEnd(bytes[current + 6], bytes[current + 7]);
            AttributeInfo[] attributes = new AttributeInfo[attributes_count];

            current = analyzeAttributes(current + 6, attributes);
            FieldInfo fieldInfo = new FieldInfo(this, access_flags, name, descriptor, attributes_count, attributes);
            fields[i] = fieldInfo;
        }

        return current;
    }

    private int analyzeMethods(int methodsOffset)
    {
        methods_count = ByteTool.uBigEnd(bytes[methodsOffset], bytes[methodsOffset + 1]);
        methods = new MethodInfo[methods_count];

        int current = methodsOffset + 2;
        for (int i = 0; i < methods_count; i++)
        {
            int access_flags = ByteTool.genFlags(bytes[current], bytes[current + 1]);
            String name = extractString(ByteTool.uBigEnd(bytes[current + 2], bytes[current + 3]));
            String descriptor = extractString(ByteTool.uBigEnd(bytes[current + 4], bytes[current + 5]));

            int attributes_count = ByteTool.uBigEnd(bytes[current + 6], bytes[current + 7]);
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
        minor_version = ByteTool.uBigEnd(bytes[4], bytes[5]);
        major_version = ByteTool.uBigEnd(bytes[6], bytes[7]);

        // constant pool count
        constant_pool_count = ByteTool.uBigEnd(bytes[8], bytes[9]);
        int over_index = analyzeConstPool();

        // access flags
        access_flags = ByteTool.genFlags(bytes[over_index], bytes[over_index + 1]);

        // this_class
        int this_class_index = ByteTool.uBigEnd(bytes[over_index + 2], bytes[over_index + 3]);
        this_class = extractStrFromClassInfo(this_class_index);

        // super_class
        int super_class_index = ByteTool.uBigEnd(bytes[over_index + 4], bytes[over_index + 5]);
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
        attributes_count = ByteTool.uBigEnd(bytes[attributesOffset], bytes[attributesOffset + 1]);
        attributes = new AttributeInfo[attributes_count];
        analyzeAttributes(attributesOffset, attributes);
    }

    public String extractStrFromClassInfo(int cl_index)
    {
        if (cl_index == 0)
        {
            return null;
        }

        try{
            int offset = cp_index[cl_index];
            assert bytes[offset] == 7;
            int utf8_index = ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]);
            return extractString(utf8_index);
        }catch (ArrayIndexOutOfBoundsException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public String extractString(int index)
    {
        int offset = cp_index[index];
        assert bytes[offset] == 1;
        int length = ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]);

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

    public int extractInt(int index)
    {
        int offset = cp_index[index];
        assert bytes[offset] == 3;

        return ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4]);
    }

    public long extractLong(int index)
    {
        return ByteTool.bigEndLong(bytes, cp_index[index] + 1);
    }

    public float extractFloat(int index)
    {
        return ByteTool.floatFrom(bytes, cp_index[index] + 1);
    }

    public double extractDouble(int index)
    {
        return ByteTool.doubleFrom(bytes, cp_index[index] + 1);
    }

    /**
     *
     * @return name=result[0], type=result[1]
     */
    public String[] extractNameType(int index)
    {
        int offset = cp_index[index];

        assert bytes[offset] == 12;
        String name = extractString(ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]));
        String type = extractString(ByteTool.uBigEnd(bytes[offset + 3], bytes[offset + 4]));

        return new String[]{name, type};
    }

    public FieldRef extractField(int index)
    {
        int offset = cp_index[index];

        assert bytes[offset] == 9; // Fieldref
        int classIndex = ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]);
        int nameTypeIndex = ByteTool.uBigEnd(bytes[offset + 3], bytes[offset + 4]);

        String className = extractStrFromClassInfo(classIndex);
        String[] nameType = extractNameType(nameTypeIndex);

        return new FieldRef(className, nameType[1], nameType[0]);
    }

    // TODO: 2016/8/8  与extractField 冗余
    public MethodRef extractMethod(int index)
    {
        int offset = cp_index[index];

        assert bytes[offset] == 10; // Methodref
        int classIndex = ByteTool.uBigEnd(bytes[offset + 1], bytes[offset + 2]);
        int nameTypeIndex = ByteTool.uBigEnd(bytes[offset + 3], bytes[offset + 4]);

        String className = extractStrFromClassInfo(classIndex);
        String[] nameType = extractNameType(nameTypeIndex);

        return new MethodRef(className, nameType[1], nameType[0]);
    }

    public Object getItem(int index)
    {
        if (index == 0)
        {
            return null;
        }

        switch (bytes[cp_index[index]])
        {
            case 1: // utf8_info
                return extractString(index);
            case 3: // int_info
                return extractInt(index);
            case 4: // float_info
                return extractFloat(index);
            case 5: // long_info
                return extractLong(index);
            case 6: // double_info
                return extractDouble(index);
        }

        throw new IndexOutOfBoundsException();
    }

    public MethodInfo getMethodInfo(String name, String descriptor)
    {
        Pair<String, String> pair = new Pair<>(name, descriptor);
        MethodInfo result = methodMap.get(pair);

        if (result != null)
        {
            return result;
        }
        else
        {
            throw new NoSuchMethodError();
        }
    }

    private void prepare()
    {
        prepared = true;
        for (FieldInfo field : fields)
        {
            if (field.isStatic())
            {
                field.initializeDefault();
            }
        }
    }

    public ClassBinary(String filePath) throws IOException
    {
        this.bytes = ByteArrayTool.fromFile(filePath);
        this.analyze();
        this.prepare();
        this.fillInMethodMap();
    }

    public ClassBinary(InputStream in) throws  IOException
    {
        this.bytes = ByteArrayTool.fromStream(in);
        this.analyze();
        this.prepare();
        // fillInMethodMap would be called later by HeapDump.initialize()
    }

    public void initialize(ThreadCopy thread)
    {
        if (initialized)
        {
            return;
        }

        // 初始化父类
        if (! super_class.equals("java/lang/Object"))
        {
            HeapDump heap = HeapDump.getInstance();
            ClassBinary father = heap.getClassBinary(super_class);
            father.initialize(thread);
        }

        initialized = true;
        // call <clinit>
        for (MethodInfo method : methods)
        {
            if (method.name.equals("<clinit>"))
            {
                MyStackFrame stackFrame = new MyStackFrame(method);
                thread.callNewMethod(stackFrame); // no return value
            }
        }

        this.fillInMethodMap();
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

    public Object getStatic(String name)
    {
        for (FieldInfo field : fields)
        {
            if (field.name.equals(name))
            {
                return field.value;
            }
        }

        throw new NoSuchFieldError();
    }

    public void putStatic(String name, Object value)
    {
        for (FieldInfo field : fields)
        {
            if (field.name.equals(name))
            {
                field.value = value;
                return;
            }
        }

        throw new NoSuchFieldError();
    }

    public void fillInMethodMap()
    {
        if (methodMap != null) // 防止多次填充
        {
            return;
        }

        methodMap = new HashMap<>();

        if (super_class != null) // not java.lang.Object
        {
            ClassBinary superClass = HeapDump.getInstance().getClassBinary(super_class);
            superClass.fillInMethodMap(); // 确保已经填充
            // put every method in superClass to this
            methodMap.putAll(superClass.methodMap);
        }

        // cover method with same name and descriptor
        for (MethodInfo method : methods)
        {
            Pair<String, String> pair = new Pair<>(method.name, method.descriptor);
            //TODO for test delete later
            if (methodMap.get(pair) != null)
            {
                System.out.println("override " + this_class + " : " + method.name);
            }
            methodMap.put(pair, method);
        }
    }






}
