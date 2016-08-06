package sissel.vm.execute;

import sissel.classinfo.ClassBinary;
import sissel.vm.EInstruction;
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
}
