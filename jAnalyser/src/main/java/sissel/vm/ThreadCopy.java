package sissel.vm;

import sissel.vm.execute.ThreadExecutor;

import java.util.Stack;

/**
 * 线程的副本
 * Created by Sissel on 2016/7/27.
 */
public class ThreadCopy
{
    private Stack<MyStackFrame> stackFrames;
    private ThreadExecutor executor;
    public ThreadState state;

    public ThreadCopy()
    {
        state = ThreadState.PREPARE;
        executor = new ThreadExecutor(this);
        stackFrames = new Stack<>();
    }

    public MyStackFrame currentStackFrame()
    {
        return stackFrames.peek();
    }

    public void pushStackFrame(MyStackFrame stackFrame)
    {
        stackFrames.push(stackFrame);
    }

    public MyStackFrame popStackFrame()
    {
        return stackFrames.pop();
    }

    public enum ThreadState
    {
        PREPARE,
        RUN,
        STOP,
        BLOCK,
        WAIT
    }

}
