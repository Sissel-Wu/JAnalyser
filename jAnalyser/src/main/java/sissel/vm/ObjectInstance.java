package sissel.vm;

import com.sun.jdi.Field;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.Value;
import sissel.classinfo.ClassBinary;
import sissel.classinfo.FieldInfo;
import sissel.classinfo.FieldRef;
import sissel.util.DescriptorHelper;

import java.util.*;

/**
 * 表示一个对象的实例
 * 构造器和初始化分开是因为初始化引用要指向已经存在的ObjectInstance
 * Created by Sissel on 2016/7/26.
 */
public class ObjectInstance
{
    // key 是字段名， value 是值(primitive对应包装器，其他对应ObjectInstance的引用)
    Map<String, Object> fieldMap;
    // 若是从vm中初始化的copy，带有引用
    ObjectReference reference;
    // 若是自己创建的数组类型，映射到这里
    Object[] array;

    // 对应的类型, primitive和数组没有
    private ClassBinary classBinary;

    // 数组有这个
    private String componentType;

    /**
     * 从vm中的ObjectReference初始化
     * @param reference 对应的ObjectReference
     */
    public ObjectInstance(ObjectReference reference)
    {
        this.reference = reference;
    }

    /**
     * 从自己的new的解释中
     * @param classBinary 对应的 Class
     */
    public ObjectInstance(ClassBinary classBinary)
    {
        this.classBinary = classBinary;
        this.fieldMap = new HashMap<>();

        // 从父类到子类放入field
        recurPutFiled(classBinary);
    }

    // 递归从父类到子类放入field
    private void recurPutFiled(ClassBinary classBinary)
    {
        String superClass = classBinary.getSuper_class();
        if (superClass != null && (!superClass.equals("java/lang/Object")))
        {
            recurPutFiled(HeapDump.getInstance().getClassBinary(superClass));
        }

        FieldInfo[] fields = classBinary.fields;
        for (FieldInfo fieldInfo : fields)
        {
            if (!fieldInfo.isStatic())
            {
                fieldMap.put(fieldInfo.name, DescriptorHelper.getDefault(fieldInfo.descriptor));
            }
        }
    }

    public ObjectInstance(String componentType, int size)
    {
        this.componentType = componentType;

        this.array = new Object[size];
    }

    /**
     * 从自己的运行中初始化primitive数组
     * @param aType 原始类型
     * @param size 数组长度
     */
    public ObjectInstance(byte aType, int size)
    {
        if (size < 0)
        {
            throw new NegativeArraySizeException();
        }

        switch (aType)
        {
            case 4:
                array = new Boolean[size];
                Arrays.fill(array, false);
                break;
            case 5:
                array = new Character[size];
                Arrays.fill(array, '\u0000');
                break;
            case 6:
                array = new Float[size];
                Arrays.fill(array, 0.0f);
                break;
            case 7:
                array = new Double[size];
                Arrays.fill(array, 0.0);
                break;
            case 8:
                array = new Byte[size];
                Arrays.fill(array, (byte)0);
                break;
            case 9:
                array = new Short[size];
                Arrays.fill(array, (short)0);
                break;
            case 10:
                array = new Integer[size];
                Arrays.fill(array, 0);
                break;
            case 11:
                array = new Long[size];
                Arrays.fill(array, 0L);
                break;
        }
    }

    /**
     * 初始化域信息, 仅是从vm来的对象需要调用
     */
    public void initialize()
    {
        fieldMap = new TreeMap<>();
        Map<Field, Value> mapInRef = reference.getValues(reference.referenceType().allFields());
        HeapDump heap = HeapDump.getInstance();

        for (Map.Entry<Field, Value> entry : mapInRef.entrySet())
        {
            Field field = entry.getKey();
            Value value = entry.getValue();
            fieldMap.put(field.name(), heap.value2Obj(value));
        }
    }

    public Object getOfArray(int index)
    {
        return array[index];
    }

    public void setOfArray(int index, Object value)
    {
        array[index] = value;
    }

    public int lengthOfArray()
    {
        return array.length;
    }

    public Object getField(FieldRef fieldRef)
    {
        return getField(fieldRef.fieldName);
    }

    public Object getField(String fieldName)
    {
        Object result = fieldMap.get(fieldName);

        if (result == null)
        {
            throw new NoSuchElementException();
        }
        else
        {
            return result;
        }
    }

    public void putField(String fieldName, Object value)
    {
        if (!fieldMap.containsKey(fieldName))
        {
            throw new NoSuchElementException();
        }

        fieldMap.put(fieldName, value);
    }

    public void putField(FieldRef fieldRef, Object value)
    {
        putField(fieldRef.fieldName, value);
    }

    public ClassBinary getClassBinary()
    {
        return classBinary;
    }

    public void setClassBinary(ClassBinary classBinary)
    {
        this.classBinary = classBinary;
    }

    public String getComponentType()
    {
        return componentType;
    }
}
