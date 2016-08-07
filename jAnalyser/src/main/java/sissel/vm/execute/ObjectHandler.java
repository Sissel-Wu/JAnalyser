package sissel.vm.execute;

import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.tools.javac.code.Attribute;
import sissel.classinfo.ClassBinary;
import sissel.classinfo.FieldRef;
import sissel.util.ByteTool;
import sissel.vm.EInstruction;
import sissel.vm.HeapDump;
import sissel.vm.MyStackFrame;
import sissel.vm.ObjectInstance;

/**
 * 对象的创建等指令
 * Created by Sissel on 2016/8/5.
 */
public class ObjectHandler
{
    public static int newObject(ClassBinary classBinary, MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        switch (instruction)
        {
            case nnew:
                break;
            case newarray:
                int count = (Integer)stackFrame.popStack();
                byte aType = byteCodes[pc + 1];
                ObjectInstance arrayRef = new ObjectInstance(aType, count);
                stackFrame.pushStack(arrayRef);
                return 2;
            case anewarray:
                break;
        }
        throw new IndexOutOfBoundsException();
    }

    // arraylength
    public static int length(MyStackFrame stackFrame)
    {
        ObjectInstance arrayRef = (ObjectInstance) stackFrame.popStack();
        stackFrame.pushStack(arrayRef.lengthOfArray());

        return 1;
    }

    public static int getPutStatic(HeapDump heap, ClassBinary cl, MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int index = ByteTool.uBigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
        FieldRef fieldRef = cl.extractField(index);

        ClassBinary targetClass = heap.getClassBinary(fieldRef.className);
        if (instruction == EInstruction.getstatic)
        {
            stackFrame.pushStack(targetClass.getStatic(fieldRef.fieldName));
        }
        else
        {
            Object value = stackFrame.popStack();
            targetClass.putStatic(fieldRef.fieldName, value);
        }

        return 3;
    }
}
