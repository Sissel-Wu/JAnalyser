package sissel.vm.execute;

import com.sun.javafx.image.ByteToBytePixelConverter;
import com.sun.tools.javac.code.Attribute;
import sissel.classinfo.ClassBinary;
import sissel.classinfo.FieldRef;
import sissel.util.ByteTool;
import sissel.vm.*;

/**
 * 对象的创建等指令
 * Created by Sissel on 2016/8/5.
 */
public class ObjectHandler
{
    public static int newObject(HeapDump heap, ThreadCopy thread, ClassBinary classBinary, MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        switch (instruction)
        {
            case nnew:
                int classIndex = ByteTool.uBigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
                String className = classBinary.extractStrFromClassInfo(classIndex);
                ClassBinary clb = heap.getClassBinary(className);
                clb.initialize(thread); // 初始化
                ObjectInstance ref = new ObjectInstance(clb);
                ref.setClassBinary(clb);
                stackFrame.pushStack(ref);
                return 3;
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

    public static int getPutSF(HeapDump heap, ThreadCopy thread, ClassBinary cl, MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int index = ByteTool.uBigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
        FieldRef fieldRef = cl.extractField(index);
        ClassBinary targetClass = heap.getClassBinary(fieldRef.className);

        switch (instruction)
        {
            case getstatic:
                targetClass.initialize(thread); // 初始化
                stackFrame.pushStack(targetClass.getStatic(fieldRef.fieldName));
                break;
            case putstatic:
                targetClass.initialize(thread); // 初始化
                Object staticValue = stackFrame.popStack();
                targetClass.putStatic(fieldRef.fieldName, staticValue);
                break;
            case getfield:
                ObjectInstance objRef = (ObjectInstance)stackFrame.popStack();
                objRef.getField(fieldRef);
                break;
            case putfield:
                Object fieldValue = stackFrame.popStack();
                ObjectInstance objRef2 = (ObjectInstance)stackFrame.popStack();
                objRef2.putField(fieldRef, fieldValue);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

        return 3;
    }
}
