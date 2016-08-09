package sissel.vm.execute;

import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;

/**
 * 处理计算
 * Created by Sissel on 2016/8/2.
 */
public class CalculateHandler
{
    public static int arithmetic(MyStackFrame stackFrame, EInstruction instruction)
    {
        Object val2 = stackFrame.popStack();
        Object val1 = stackFrame.popStack();
        switch (instruction)
        {
            case iadd:
                stackFrame.pushStack((Integer)val1 + (Integer)val2);
                break;
            case ladd:
                stackFrame.pushStack((Long)val1 + (Long)val2);
                break;
            case fadd:
                stackFrame.pushStack((Float)val1 + (Float)val2);
                break;
            case dadd:
                stackFrame.pushStack((Double)val1 + (Double)val2);
                break;
            case isub:
                stackFrame.pushStack((Integer)val1 - (Integer)val2);
                break;
            case lsub:
                stackFrame.pushStack((Long)val1 - (Long)val2);
                break;
            case fsub:
                stackFrame.pushStack((Float)val1 - (Float)val2);
                break;
            case dsub:
                stackFrame.pushStack((Double)val1 - (Double)val2);
                break;
            case imul:
                stackFrame.pushStack((Integer)val1 * (Integer)val2);
                break;
            case lmul:
                stackFrame.pushStack((Long)val1 * (Long)val2);
                break;
            case fmul:
                stackFrame.pushStack((Float)val1 * (Float)val2);
                break;
            case dmul:
                stackFrame.pushStack((Double)val1 * (Double)val2);
                break;
            case idiv:
                stackFrame.pushStack((Integer)val1 / (Integer)val2);
                break;
            case ldiv:
                stackFrame.pushStack((Long)val1 / (Long)val2);
                break;
            case fdiv:
                stackFrame.pushStack((Float)val1 / (Float)val2);
                break;
            case ddiv:
                stackFrame.pushStack((Double)val1 / (Double)val2);
                break;
            case irem:
                stackFrame.pushStack((Integer)val1 % (Integer)val2);
                break;
            case lrem:
                stackFrame.pushStack((Long)val1 % (Long)val2);
                break;
            case frem:
                stackFrame.pushStack((Float)val1 % (Float)val2);
                break;
            case drem:
                stackFrame.pushStack((Double)val1 % (Double)val2);
                break;
        }

        return 1;
    }

    public static int neg(MyStackFrame stackFrame, EInstruction instruction)
    {
        switch (instruction)
        {
            case ineg:
                stackFrame.pushStack(-(Integer)stackFrame.popStack());
                break;
            case lneg:
                stackFrame.pushStack(-(Long)stackFrame.popStack());
                break;
            case fneg:
                stackFrame.pushStack(-(Float)stackFrame.popStack());
                break;
            case dneg:
                stackFrame.pushStack(-(Double)stackFrame.popStack());
                break;
        }

        return 1;
    }

    public static int iinc(MyStackFrame stackFrame, int index, int val)
    {
        Integer target = (Integer) stackFrame.getLocal(index);
        stackFrame.setLocal(index, val + target);

        return 3;
    }

    public static int raw(MyStackFrame stackFrame, EInstruction instruction)
    {
        Object val2 = stackFrame.popStack();
        Object val1 = stackFrame.popStack();

        switch (instruction)
        {
            case ishl:
                stackFrame.pushStack((Integer)val1 << (Integer)val2);
                break;
            case lshl:
                stackFrame.pushStack((Long)val1 << (Long)val2);
                break;
            case ishr:
                stackFrame.pushStack((Integer)val1 >> (Integer)val2);
                break;
            case lshr:
                stackFrame.pushStack((Long)val1 >> (Long)val2);
                break;
            case iushr:
                stackFrame.pushStack((Integer)val1 >>> (Integer)val2);
                break;
            case lushr:
                stackFrame.pushStack((Long)val1 >>> (Long)val2);
                break;
            case iand:
                stackFrame.pushStack((Integer)val1 & (Integer)val2);
                break;
            case land:
                stackFrame.pushStack((Long)val1 & (Long)val2);
                break;
            case ior:
                stackFrame.pushStack((Integer)val1 | (Integer)val2);
                break;
            case lor:
                stackFrame.pushStack((Long)val1 | (Long)val2);
                break;
            case ixor:
                stackFrame.pushStack((Integer)val1 ^ (Integer)val2);
                break;
            case lxor:
                stackFrame.pushStack((Long)val1 ^ (Long)val2);
                break;
        }

        return 1;
    }

    public static int transform(MyStackFrame stackFrame, EInstruction instruction)
    {
        switch (instruction)
        {
            case i2l:
                int i1 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((long)i1);
                break;
            case i2f:
                int i2 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((float)i2);
                break;
            case i2d:
                int i3 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((double)i3);
                break;
            case l2i:
                long l1 = (Long)stackFrame.popStack();
                stackFrame.pushStack((int)l1);
                break;
            case l2f:
                long l2 = (Long)stackFrame.popStack();
                stackFrame.pushStack((float)l2);
                break;
            case l2d:
                long l3 = (Long)stackFrame.popStack();
                stackFrame.pushStack((double)l3);
                break;
            case f2i:
                float f1 = (Float)stackFrame.popStack();
                stackFrame.pushStack((int)f1);
                break;
            case f2l:
                float f2 = (Float)stackFrame.popStack();
                stackFrame.pushStack((long)f2);
                break;
            case f2d:
                float f3 = (Float)stackFrame.popStack();
                stackFrame.pushStack((double)f3);
                break;
            case d2i:
                double d1 = (Double)stackFrame.popStack();
                stackFrame.pushStack((int)d1);
                break;
            case d2l:
                double d2 = (Double)stackFrame.popStack();
                stackFrame.pushStack((long)d2);
                break;
            case d2f:
                double d3 = (Double)stackFrame.popStack();
                stackFrame.pushStack((float)d3);
                break;
            case i2b:
                int i4 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((int)(byte)i4);
                break;
            case i2c:
                int i5 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((int)(char)i5);
                break;
            case i2s:
                int i6 = (Integer)stackFrame.popStack();
                stackFrame.pushStack((int)(short)i6);
                break;
        }
        return 1;
    }

    public static int compare(MyStackFrame stackFrame, EInstruction instruction)
    {
        Object val2 = stackFrame.popStack();
        Object val1 = stackFrame.popStack();
        switch (instruction)
        {
            case lcmp:
                Long l1 = (Long)val1, l2 = (Long)val2;
                if (l1 > l2)
                {
                    stackFrame.pushStack(1);
                }
                else if (l1 < l2)
                {
                    stackFrame.pushStack(-1);
                }
                else
                {
                    stackFrame.pushStack(0);
                }
                break;
            case fcmpl:
            case fcmpg:
                Float f1 = (Float)val1, f2 = (Float)val2;
                if (f1.equals(Float.NaN) || f2.equals(Float.NaN))
                {
                    if (instruction == EInstruction.fcmpl)
                    {
                        stackFrame.pushStack(-1);
                    }
                    else
                    {
                        stackFrame.pushStack(1);
                    }
                }
                else if (f1 > f2)
                {
                    stackFrame.pushStack(1);
                }
                else if (f1 < f2)
                {
                    stackFrame.pushStack(-1);
                }
                else
                {
                    stackFrame.pushStack(0);
                }
                break;
            case dcmpl:
            case dcmpg:
                Double d1 = (Double)val1, d2 = (Double)val2;
                if (d1.equals(Double.NaN) || d2.equals(Double.NaN))
                {
                    if (instruction == EInstruction.dcmpl)
                    {
                        stackFrame.pushStack(-1);
                    }
                    else
                    {
                        stackFrame.pushStack(1);
                    }
                }
                else if (d1 > d2)
                {
                    stackFrame.pushStack(1);
                }
                else if (d1 < d2)
                {
                    stackFrame.pushStack(-1);
                }
                else
                {
                    stackFrame.pushStack(0);
                }
                break;
        }
        return 1;
    }
}
