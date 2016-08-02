package sissel.vm.execute;

import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;

/**
 * 处理计算
 * Created by Sissel on 2016/8/2.
 */
public class CalculateHandler
{
    public static int add(MyStackFrame stackFrame, EInstruction instruction)
    {
        switch (instruction)
        {
            case iadd:
                stackFrame.pushStack((Integer)stackFrame.popStack() + (Integer)stackFrame.popStack());
                break;
            case ladd:
                stackFrame.pushStack((Long)stackFrame.popStack() + (Long)stackFrame.popStack());
                break;
            case fadd:
                stackFrame.pushStack((Float)stackFrame.popStack() + (Float)stackFrame.popStack());
                break;
            case dadd:
                stackFrame.pushStack((Double)stackFrame.popStack() + (Double)stackFrame.popStack());
                break;
        }

        return 1;
    }
}
