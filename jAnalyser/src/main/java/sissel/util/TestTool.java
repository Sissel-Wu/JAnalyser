package sissel.util;

import sissel.classinfo.ClassBinary;
import sissel.test_cases.Add;
import sissel.vm.HeapDump;
import sissel.vm.MyStackFrame;
import sissel.vm.ThreadCopy;

import java.io.IOException;

/**
 * 测试指令的工具类
 * Created by Sissel on 2016/8/4.
 */
public class TestTool
{
    public static Object test(String className, String methodName, String descriptor) throws IOException
    {
        String path = Add.class.getResource(className + ".class").getPath();
        HeapDump heapDump = HeapDump.newInstance();
        ClassBinary clbin = new ClassBinary(path);
        MyStackFrame stackFrame = new MyStackFrame(clbin.getMethodInfo(methodName, descriptor));

        heapDump.addClassBinary(clbin);

        ThreadCopy threadCopy = new ThreadCopy(heapDump);
        return threadCopy.callNewMethod(stackFrame);
    }
}
