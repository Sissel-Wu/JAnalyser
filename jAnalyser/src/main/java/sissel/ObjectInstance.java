package sissel;

import com.sun.jdi.*;
import util.var;

import java.util.List;
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
    ObjectReference reference;

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
        else if (value instanceof ObjectReference)
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

    public ObjectInstance(ObjectReference reference)
    {
        this.reference = reference;
    }
}
