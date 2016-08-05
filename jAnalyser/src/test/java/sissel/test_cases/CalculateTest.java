package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

import static org.junit.Assert.*;

/**
 * 测试指令集
 * Created by Sissel on 2016/8/4.
 */
public class CalculateTest
{

    @Test
    public void testTestSub() throws Exception
    {
        TestTool.test("Calculate", "testSub", "()I");
    }

    @Test
    public void testTestMul() throws Exception
    {
        TestTool.test("Calculate", "testMul", "()I");
    }

    @Test
    public void testTestDiv() throws Exception
    {
        TestTool.test("Calculate", "testDiv", "()I");
    }

    @Test
    public void testTestNeg() throws Exception
    {
        TestTool.test("Calculate", "testNeg", "()I");
    }

    @Test
    public void testTestSH() throws Exception
    {
        TestTool.test("Calculate", "testSH", "()I");
    }

    @Test
    public void testTestInc() throws Exception
    {
        TestTool.test("Calculate", "testInc", "()I");
    }

    @Test
    public void testTestOther() throws Exception
    {
        TestTool.test("Calculate", "testOther", "()I");
    }

    @Test
    public void testTestMod() throws Exception
    {
        TestTool.test("Calculate", "testMod", "()I");
    }
}