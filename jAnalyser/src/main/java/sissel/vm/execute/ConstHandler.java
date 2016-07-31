package sissel.vm.execute;

import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;

/**
 * 处理常量指令
 * Created by Sissel on 2016/7/31.
 */
public class ConstHandler
{
    public static void pushInstant(MyStackFrame stackFrame, EInstruction instruction)
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
    }

    
}
