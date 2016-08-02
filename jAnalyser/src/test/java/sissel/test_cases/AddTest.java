package sissel.test_cases;

import sissel.classinfo.ClassBinary;
import sissel.vm.MyStackFrame;
import sissel.vm.ThreadCopy;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * 测试Add的方法里面的指令的情况
 * Created by Sissel on 2016/8/2.
 */
public class AddTest
{
    @org.junit.Test
    public void testAdd()
    {
        String path = Add.class.getResource("Add.class").getPath();
        try
        {
            ClassBinary clbin = new ClassBinary(path);
            MyStackFrame stackFrame = new MyStackFrame(clbin.getMethodInfo("test", "()I"));

            ThreadCopy threadCopy = new ThreadCopy();
            threadCopy.pushStackFrame(stackFrame);
            threadCopy.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}