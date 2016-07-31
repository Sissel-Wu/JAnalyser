package sissel.vm.execute;

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

        int codeLength = methodInfo.code_length;
        byte[] byteCodes = methodInfo.code;
        while (pc < codeLength)
        {
            EInstruction instruction = EInstruction.forr(byteCodes[pc++]);

        }
    }
}
