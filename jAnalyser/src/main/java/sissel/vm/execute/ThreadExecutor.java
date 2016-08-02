package sissel.vm.execute;

import sissel.classinfo.ClassBinary;
import sissel.classinfo.MethodInfo;
import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;
import sissel.vm.ThreadCopy;

/**
 * scope: 每个线程的执行
 * Created by Sissel on 2016/7/31.
 */
public class ThreadExecutor
{
    private ThreadCopy thread;
    private int pc;

    public ThreadExecutor(ThreadCopy thread)
    {
        this.thread = thread;
    }

    public void start()
    {
        pc = 0;
        continues();
    }

    public void continues()
    {
        thread.state = ThreadCopy.ThreadState.RUN;

        MyStackFrame stackFrame = thread.currentStackFrame();
        MethodInfo methodInfo = stackFrame.getMethodInfo();
        ClassBinary classBinary = stackFrame.getMethodInfo().classBinary;

        int codeLength = methodInfo.code_length;
        byte[] byteCodes = methodInfo.code;
        while (pc < codeLength)
        {
            EInstruction instruction = EInstruction.forr(byteCodes[pc]);
            int byteCode = instruction.byteCode;
            if (byteCode > 0x00 && byteCode <= 0x0f) // xconst
            {
                pc += ConstHandler.pushInstant(stackFrame, instruction);
            }
            else if (byteCode >= 0x10 && byteCode <= 0x11) // bipush, sipush
            {
                pc += ConstHandler.pushLaterInstant(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0x12 && byteCode <= 0x14) // ldc, ldc_w, ldc2_w
            {
                pc += ConstHandler.pushConstant(classBinary, stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0x1a && byteCode <= 0x2d) // xload_<n>
            {
                pc += ConstHandler.pushLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x3b && byteCode <= 0x4e) // xstore_<n>
            {
                pc += ConstHandler.storeLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x60 && byteCode <= 0x63) // iadd
            {
                pc += CalculateHandler.add(stackFrame, instruction);
            }
        }
    }
}
