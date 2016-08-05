package sissel.test_cases;

import sissel.classinfo.ClassBinary;
import sissel.util.TestTool;
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
    public void testInt()
    {
        TestTool.test("Add", "testInt", "()I");
    }

    @org.junit.Test
    public void testLong()
    {
        TestTool.test("Add", "testLong", "()J");
    }

    @org.junit.Test
    public void testFloat()
    {
        TestTool.test("Add", "testFloat", "()F");
    }

    @org.junit.Test
    public void testDouble()
    {
        TestTool.test("Add", "testDouble", "()D");
    }
}