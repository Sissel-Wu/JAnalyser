package sissel;

import com.sun.jdi.*;
import util.var;

import java.util.*;

/**
 * 堆的副本，单例模式
 * Created by Sissel on 2016/7/26.
 */
public class HeapDump
{
    private static HeapDump singleton;

    private VirtualMachine vm;
    Map<String, Map<ObjectReference, ObjectInstance>> objMap;

    public static HeapDump newInstance(VirtualMachine vm)
    {
        singleton = new HeapDump(vm);

        return singleton;
    }

    public static HeapDump getInstance()
    {
        return singleton;
    }

    private HeapDump(VirtualMachine vm)
    {
        this.vm = vm;
        objMap = new TreeMap<>();

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
        }
    }

    public void initialize()
    {
        List<ReferenceType> allClasses = vm.allClasses();
        // 初始化每个instance
        for (ReferenceType aClass : allClasses)
        {
            Map<ObjectReference, ObjectInstance> objectInstances = objMap.get(aClass.name());

            for (ObjectInstance instance : objectInstances.values())
            {
                instance.initialize();
            }
        }
    }

    public int countClassInstance(String className)
    {
        Map<ObjectReference, ObjectInstance> objInstances = objMap.get(className);
        return objInstances.size();
    }

    public ObjectInstance getByReference(ObjectReference reference)
    {
        Map<ObjectReference, ObjectInstance> instanceMap = objMap.get(reference.referenceType().name());
        return instanceMap.get(reference);
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
