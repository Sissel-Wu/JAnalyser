package sissel.vm;

import sissel.classinfo.MethodInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 模拟虚拟机里面的栈帧
 * Created by Sissel on 2016/7/28.
 */
public class MyStackFrame
{
    private Stack<Object> operandStack;
    private Object[] localVariables;
    private MethodInfo methodInfo;

    public MethodInfo getMethodInfo()
    {
        return methodInfo;
    }

    /**
     * 方法调用时，创建一个栈帧
     * @param methodInfo 方法的信息(从class中来)
     */
    public MyStackFrame(MethodInfo methodInfo)
    {
        this.methodInfo = methodInfo;
        this.operandStack = new Stack<>();
        this.localVariables = new Object[methodInfo.max_locals];
    }

    public void pushStack(Object obj)
    {
        operandStack.push(obj);
    }

    public Object popStack()
    {
        return operandStack.pop();
    }

    public Object load(int i)
    {
        operandStack.push(localVariables[i]);
        return localVariables[i];
    }

    public void store(int i)
    {
        localVariables[i] = operandStack.pop();
    }

    public void setLocal(int i, Object obj)
    {
        localVariables[i] = obj;
    }

    public Object getLocal(int i)
    {
        return localVariables[i];
    }

    public Object peek()
    {
        return operandStack.peek();
    }
}
