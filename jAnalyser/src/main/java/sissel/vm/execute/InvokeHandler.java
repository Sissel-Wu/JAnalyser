package sissel.vm.execute;

import sissel.classinfo.ClassBinary;
import sissel.classinfo.MethodInfo;
import sissel.classinfo.MethodRef;
import sissel.util.ByteTool;
import sissel.util.DescriptorHelper;
import sissel.vm.*;

import java.util.List;
import java.util.Stack;

/**
 * 处理方法调用和返回
 * Created by Sissel on 2016/8/8.
 */
public class InvokeHandler
{

    public static int invoke(HeapDump heap, ThreadCopy thread, ClassBinary classBinary, MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int index = ByteTool.uBigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
        MethodRef methodRef = classBinary.extractMethod(index);
        List<String> splitDescriptor = DescriptorHelper.split(methodRef.descriptor);
        int paramCount = splitDescriptor.size() - 1;

        // prepare args
        Stack<Object> tmpStack = new Stack<>();
        for (int i = 0; i < paramCount; i++)
        {
            tmpStack.push(stackFrame.popStack());
        }

        // 寻找具体的方法
        MethodInfo calledMethod;
        ClassBinary belongClass;
        ObjectInstance objRef = null;
        switch (instruction)
        {
            case invokestatic:
                belongClass = heap.getClassBinary(methodRef.className);
                belongClass.initialize(thread);
                calledMethod = belongClass.getMethodInfo(methodRef.methodName, methodRef.descriptor);
                break;
            case invokespecial:
                belongClass = heap.getClassBinary(methodRef.className);
                objRef = (ObjectInstance)stackFrame.popStack();
                calledMethod = belongClass.getMethodInfo(methodRef.methodName, methodRef.descriptor);
                break;
            case invokevirtual:
            case invokeinterface:
                objRef = (ObjectInstance)stackFrame.popStack();
                calledMethod = objRef.getClassBinary().getMethodInfo(methodRef.methodName, methodRef.descriptor);
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

        // this object
        if (instruction != EInstruction.invokestatic)
        {
            tmpStack.push(objRef);
        }

        // new stack frame
        MyStackFrame newStackFrame = new MyStackFrame(calledMethod);
        // 传入实参
        int size = tmpStack.size();
        for (int i = 0; i < size; i++)
        {
            newStackFrame.setLocal(i, tmpStack.pop());
        }

        // 方法调用和返回
        Object result = thread.callNewMethod(newStackFrame);
        if (!splitDescriptor.get(splitDescriptor.size() - 1).equals("V"))
        {
            stackFrame.pushStack(result);
        }

        if (instruction == EInstruction.invokeinterface)
        {
            return 5;
        }
        else
        {
            return 3;
        }
    }
}
