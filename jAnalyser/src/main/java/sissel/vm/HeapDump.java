package sissel.vm;

import com.sun.jdi.*;
import sissel.classinfo.ClassBinary;
import sissel.util.LoadClassHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 堆的副本，单例模式
 * Created by Sissel on 2016/7/26.
 */
public class HeapDump
{
    private static HeapDump singleton;

    private VirtualMachine vm;
    Map<String, Map<ObjectReference, ObjectInstance>> objMap;
    Map<String, ClassBinary> classMap;

    public static HeapDump newInstance(VirtualMachine vm)
    {
        singleton = new HeapDump(vm);

        return singleton;
    }

    public static HeapDump newInstance()
    {
        singleton = new HeapDump();

        return singleton;
    }

    public static HeapDump getInstance()
    {
        return singleton;
    }

    private HeapDump()
    {
        objMap = new TreeMap<>();
        classMap = new TreeMap<>();
    }

    private HeapDump(VirtualMachine vm)
    {
        this();
        this.vm = vm;

        List<ReferenceType> allClasses = vm.allClasses();
        for (ReferenceType aClass : allClasses)
        {
            Map<ObjectReference, ObjectInstance> objectInstances = new HashMap<>();

            List<ObjectReference> objRefs = aClass.instances(0); // 0 means all
            objRefs.stream().filter(objRef -> !objRef.isCollected()).forEach(objRef ->
            {
                ObjectInstance objectInstance = new ObjectInstance(objRef);
                objectInstances.put(objRef, objectInstance);
            });

            objMap.put(aClass.name(), objectInstances);

            if (!(aClass instanceof ArrayType))
            {
                ClassBinary classBinary = LoadClassHelper.load(aClass);
                if (classBinary != null)
                {
                    classMap.put(aClass.name(), classBinary);
                }
                else
                {
                    System.err.println("load fail: " + aClass.name());
                }
            }
        }
    }

    public void initialize()
    {
        List<ReferenceType> allClasses = vm.allClasses();

        // 初始化需要初始化的ClassBinary
        for (ReferenceType aClass : allClasses)
        {
            if ((!(aClass instanceof ArrayType)) && aClass.isInitialized())
            {
                ClassBinary classBinary = classMap.get(aClass.name());
                // 初始化
                classBinary.initialized = true;
                List<Field> fields = aClass.fields();
                fields.stream().filter(Field::isStatic).forEach(
                        field -> classBinary.putStatic(field.name(), value2Obj(aClass.getValue(field)))
                );
                // 方法表
                classBinary.fillInMethodMap();
            }
        }

        // 初始化每个instance
        for (ReferenceType aClass : allClasses)
        {
            ClassBinary classBinary = classMap.get(aClass.name());
            Map<ObjectReference, ObjectInstance> objectInstances = objMap.get(aClass.name());

            objectInstances.values().forEach(o ->
                    {
                        o.initialize();
                        o.setClassBinary(classBinary);
                    }
            );
        }
    }

    public void addClassBinary(ClassBinary classBinary)
    {
        classMap.put(classBinary.getThis_class(), classBinary);
    }

    /**
     * 寻找ClassBinary，若没有，进行解析
     * @param className 包之间用“.”或“/”分割均可，最后以类名结束
     * @return 指定的ClassBinary
     */
    public ClassBinary getClassBinary(String className)
    {
        String keyName = className.replaceAll("/", ".");
        ClassBinary classBinary = classMap.get(keyName);
        if (classBinary == null)
        {
            // 加载类
            ClassBinary newCB = LoadClassHelper.load(keyName);
            if (newCB != null)
            {
                classMap.put(keyName, newCB);
            }
            return newCB;
        }
        else
        {
            return classBinary;
        }
    }

    public ObjectInstance getByReference(Object ref)
    {
        if (ref instanceof ObjectReference)
        {
            ObjectReference reference = (ObjectReference)ref;
            Map<ObjectReference, ObjectInstance> instanceMap = objMap.get(reference.referenceType().name());
            return instanceMap.get(reference);
        }
        else if (ref instanceof ObjectInstance)
        {
            return (ObjectInstance) ref;
        }
        else
        {
            throw new ClassCastException();
        }
    }

    public Object value2Obj(Value value)
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
            return getByReference(value);
        }
        else if (value instanceof VoidValue)
        {
            System.err.println("ObjectInstance::value2Obj: void value");
            return null;
        }

        assert value == null;
        return null;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Map<ObjectReference, ObjectInstance>> entry : objMap.entrySet())
        {
            sb.append(entry.getKey());
            sb.append(": ");
            sb.append(entry.getValue().size()).append("\n");
        }
        return sb.toString();
    }
}
