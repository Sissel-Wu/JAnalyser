package sissel.test_cases;

import sissel.util.TestTool;

import java.io.IOException;

/**
 * 测试Add的方法里面的指令的情况
 * Created by Sissel on 2016/8/2.
 */
public class AddTest
{

    @org.junit.Test
    public void testInt() throws IOException
    {
        TestTool.test("Add", "testInt", "()I");
    }

    @org.junit.Test
    public void testLong() throws IOException
    {
        TestTool.test("Add", "testLong", "()J");
    }

    @org.junit.Test
    public void testFloat() throws IOException
    {
        TestTool.test("Add", "testFloat", "()F");
    }

    @org.junit.Test
    public void testDouble() throws IOException
    {
        TestTool.test("Add", "testDouble", "()D");
    }
}