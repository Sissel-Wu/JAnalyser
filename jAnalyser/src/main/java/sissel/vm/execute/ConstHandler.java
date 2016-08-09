package sissel.vm.execute;

import sissel.classinfo.ClassBinary;
import sissel.util.ByteTool;
import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;
import sissel.vm.ObjectInstance;

/**
 * 处理常量指令
 * Created by Sissel on 2016/7/31.
 */
public class ConstHandler
{
    public static int pushInstant(MyStackFrame stackFrame, EInstruction instruction)
    {
        switch (instruction)
        {
            case aconst_null:
                stackFrame.pushStack(null);
                break;
            case iconst_m1:
                stackFrame.pushStack(-1);
                break;
            case iconst_0:
                stackFrame.pushStack(0);
                break;
            case iconst_1:
                stackFrame.pushStack(1);
                break;
            case iconst_2:
                stackFrame.pushStack(2);
                break;
            case iconst_3:
                stackFrame.pushStack(3);
                break;
            case iconst_4:
                stackFrame.pushStack(4);
                break;
            case iconst_5:
                stackFrame.pushStack(5);
                break;
            case lconst_0:
                stackFrame.pushStack(0L);
                break;
            case lconst_1:
                stackFrame.pushStack(1L);
                break;
            case fconst_0:
                stackFrame.pushStack(0f);
                break;
            case fconst_1:
                stackFrame.pushStack(1f);
                break;
            case fconst_2:
                stackFrame.pushStack(2f);
                break;
            case dconst_0:
                stackFrame.pushStack(0.0);
                break;
            case dconst_1:
                stackFrame.pushStack(1.0);
                break;
        }

        return 1;
    }

    /**
     *
     * @param stackFrame 栈帧
     * @param instruction 指令
     * @param bytes 全部的字节码
     * @param offset 指令（bipush或sipush）的偏移位置
     * @return 这次执行的长度
     */
    public static int pushLaterInstant(MyStackFrame stackFrame, EInstruction instruction, byte[] bytes, int offset)
    {
        int instant;
        if (instruction == EInstruction.bipush)
        {
            instant = ByteTool.bigEnd(bytes[offset + 1]);
            stackFrame.pushStack(instant);
            return 2;
        }
        else
        {
            assert instruction == EInstruction.sipush;
            instant = ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2]);
            stackFrame.pushStack(instant);
            return 3;
        }
    }

    public static int pushConstant(ClassBinary cl, MyStackFrame stackFrame, EInstruction instruction, byte[] bytes, int offset)
    {
        int index;
        switch (instruction)
        {
            case ldc:
                index = ByteTool.bigEnd(bytes[offset + 1]);
                stackFrame.pushStack(cl.getItem(index));
                return 2;
            case ldc_w:
            case ldc2_w:
                index = ByteTool.bigEnd(bytes[offset + 1], bytes[offset + 2]);
                stackFrame.pushStack(cl.getItem(index));
                return 3;
        }

        throw new UnknownError("instruction not match: " + instruction.name());
    }

    public static int pushLocal(MyStackFrame stackFrame, EInstruction instruction)
    {
        int index = -1;
        int instructionByte = instruction.byteCode;
        switch (instruction)
        {
            // i
            case iload_0:
            case iload_1:
            case iload_2:
            case iload_3:
                index = instructionByte - 0x1a;
                break;
            // l
            case lload_0:
            case lload_1:
            case lload_2:
            case lload_3:
                index = instructionByte - 0x1e;
                break;
            // f
            case fload_0:
            case fload_1:
            case fload_2:
            case fload_3:
                index = instructionByte - 0x22;
                break;
            // d
            case dload_0:
            case dload_1:
            case dload_2:
            case dload_3:
                index = instructionByte - 0x26;
                break;
            // a
            case aload_0:
            case aload_1:
            case aload_2:
            case aload_3:
                index = instructionByte - 0x2a;
                break;
        }
        stackFrame.pushStack(stackFrame.getLocal(index));

        return 1;
    }

    // xload index
    public static void pushLocal(MyStackFrame stackFrame, EInstruction instruction, int index)
    {
        assert instruction.byteCode >= 0x15 && instruction.byteCode <= 0x19;

        stackFrame.pushStack(stackFrame.getLocal(index));
    }

    // xstore_<n>
    public static int storeLocal(MyStackFrame stackFrame, EInstruction instruction)
    {
        int index = -1;
        int instructionByte = instruction.byteCode;
        switch (instruction)
        {
            case istore_0:
            case istore_1:
            case istore_2:
            case istore_3:
                index = instructionByte - 0x3b;
                break;
            case lstore_0:
            case lstore_1:
            case lsotre_2:
            case lstore_3:
                index = instructionByte - 0x3f;
                break;
            case fstore_0:
            case fstore_1:
            case fstore_2:
            case fstore_3:
                index = instructionByte - 0x43;
                break;
            case dstore_0:
            case dstore_1:
            case dstore_2:
            case dstore_3:
                index = instructionByte - 0x47;
                break;
            case astore_0:
            case astore_1:
            case astore_2:
            case astore_3:
                index = instructionByte - 0x4b;
                break;
        }
        stackFrame.setLocal(index, stackFrame.popStack());
        return 1;
    }

    public static void storeLocal(MyStackFrame stackFrame, EInstruction instruction, int index)
    {
        // xstore
        assert instruction.byteCode >= 0x36 && instruction.byteCode <= 0x3a;

        stackFrame.setLocal(index, stackFrame.popStack());
    }

    public static int pushArrayLocal(MyStackFrame stackFrame, EInstruction instruction)
    {
        // xaload
        assert instruction.byteCode >= 0x2e && instruction.byteCode <= 0x35;

        Integer index = (Integer) stackFrame.popStack();
        ObjectInstance objectInstance = (ObjectInstance)stackFrame.popStack();
        stackFrame.pushStack(objectInstance.getOfArray(index));

        return 1;
    }

    public static int storeArrayLocal(MyStackFrame stackFrame, EInstruction instruction)
    {
        // xastore
        assert instruction.byteCode >= 0x4f && instruction.byteCode <= 0x56;

        Object value = stackFrame.popStack();
        Integer index = (Integer) stackFrame.popStack();
        ObjectInstance arrayRef = (ObjectInstance)stackFrame.popStack();
        arrayRef.setOfArray(index, value);

        return 1;
    }

    public static int stackOp(MyStackFrame stackFrame, EInstruction instruction)
    {
        Object top, nonTop;
        switch (instruction)
        {
            case pop:
                stackFrame.popStack();
                break;
            case pop2:
                top = stackFrame.popStack();
                if (!(top instanceof Long || top instanceof Double) )
                {
                    stackFrame.popStack();
                }
                break;
            case dup:
                stackFrame.pushStack(stackFrame.peek());
                break;
            case dup_x1:
                top = stackFrame.popStack();
                nonTop = stackFrame.popStack();
                stackFrame.pushStack(top);
                stackFrame.pushStack(nonTop);
                stackFrame.pushStack(top);
                break;
            case dup_x2:
                top = stackFrame.popStack();
                nonTop = stackFrame.popStack();
                if (nonTop instanceof Long || nonTop instanceof Double)
                {
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                }
                else
                {
                    Object third = stackFrame.popStack();
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(third);
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                }
                break;
            case dup2:
                top = stackFrame.popStack();
                if (top instanceof Long || top instanceof Double)
                {
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(top);
                }
                else
                {
                    nonTop = stackFrame.peek();
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                }
                break;
            case dup2_x1:
                top = stackFrame.popStack();
                if (top instanceof Long || top instanceof Double)
                {
                    nonTop = stackFrame.popStack();
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                }
                else
                {
                    nonTop = stackFrame.popStack();
                    Object third = stackFrame.popStack();
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                    stackFrame.pushStack(third);
                    stackFrame.pushStack(nonTop);
                    stackFrame.pushStack(top);
                }
                break;
            case dup2_x2:
                top = stackFrame.popStack();
                nonTop = stackFrame.popStack();
                if (top instanceof Long || top instanceof Double)
                {
                    if (nonTop instanceof Long || nonTop instanceof Double)
                    {
                        stackFrame.pushStack(top);
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                    }
                    else
                    {
                        Object third = stackFrame.popStack();
                        stackFrame.pushStack(top);
                        stackFrame.pushStack(third);
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                    }
                }
                else
                {
                    Object third = stackFrame.popStack();
                    if (third instanceof Long || third instanceof Double)
                    {
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                        stackFrame.pushStack(third);
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                    }
                    else
                    {
                        Object fourth = stackFrame.popStack();
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                        stackFrame.pushStack(fourth);
                        stackFrame.pushStack(third);
                        stackFrame.pushStack(nonTop);
                        stackFrame.pushStack(top);
                    }
                }
                break;
            case swap:
                top = stackFrame.popStack();
                nonTop = stackFrame.popStack();
                stackFrame.pushStack(top);
                stackFrame.pushStack(nonTop);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

        return 1;
    }
}
