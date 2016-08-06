package sissel.vm;

import com.sun.jdi.*;

import java.util.Map;
import java.util.TreeMap;

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

    /**
     * 从vm中的ObjectReference初始化
     * @param reference 对应的ObjectReference
     */
    public ObjectInstance(ObjectReference reference)
    {
        this.reference = reference;
    }

    /**
     * 从自己的运行中初始化primitive数组
     * @param aType 原始类型
     * @param size 数组长度
     */
    public ObjectInstance(byte aType, int size)
    {
        this.reference = null;
        this.fieldMap = null;

        if (size < 0)
        {
            throw new NegativeArraySizeException();
        }

        switch (aType)
        {
            case 4:
                array = new Boolean[size];
                break;
            case 5:
                array = new Character[size];
                break;
            case 6:
                array = new Float[size];
                break;
            case 7:
                array = new Double[size];
                break;
            case 8:
                array = new Byte[size];
                break;
            case 9:
                array = new Short[size];
                break;
            case 10:
                array = new Integer[size];
                break;
            case 11:
                array = new Long[size];
                break;
        }
    }

    private Object value2Obj(Value value)
    {
        // primitive value
        if (value instanceof BooleanValue)
        {
            return ((BooleanValue) value).value();
        }
        else if (value instanceof ByteValue)
        {
            return ((ByteValue)value).value();
        }
        else if (value instanceof CharValue)
        {
            return ((CharValue)value).value();
        }
        else if (value instanceof DoubleValue)
        {
            return ((DoubleValue)value).value();
        }
        else if (value instanceof FloatValue)
        {
            return ((FloatValue)value).value();
        }
        else if (value instanceof IntegerValue)
        {
            return ((IntegerValue)value).value();
        }
        else if (value instanceof LongValue)
        {
            return ((LongValue)value).value();
        }
        else if (value instanceof ShortValue)
        {
            return ((ShortValue)value).value();
        }
        // object reference
        else if (value instanceof ObjectReference)// TODO: 2016/7/30 数组的值
        {
            HeapDump heapDump = HeapDump.getInstance();
            return heapDump.getByReference((ObjectReference) value);
        }
        else if (value instanceof VoidValue)
        {
            System.err.println("ObjectInstance::value2Obj: void value");
            return null;
        }

        assert value == null;
        return null;
    }

    /**
     * 初始化域信息
     */
    public void initialize()
    {
        fieldMap = new TreeMap<>();
        Map<Field, Value> mapInRef = reference.getValues(reference.referenceType().allFields());

        for (Map.Entry<Field, Value> entry : mapInRef.entrySet())
        {
            Value value = entry.getValue();
            fieldMap.put(entry.getKey().name(), value2Obj(value));
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
}
