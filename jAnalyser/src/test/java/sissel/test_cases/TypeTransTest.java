package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

import static org.junit.Assert.*;

/**
 * 类型转换指令的测试
 * Created by Sissel on 2016/8/5.
 */
public class TypeTransTest
{

    @Test
    public void testI2x() throws Exception
    {
        TestTool.test("TypeTrans", "i2x", "()V");
    }

    @Test
    public void testL2x() throws Exception
    {
        TestTool.test("TypeTrans", "l2x", "()V");
    }

    @Test
    public void testF2x() throws Exception
    {
        TestTool.test("TypeTrans", "f2x", "()V");
    }

    @Test
    public void testD2x() throws Exception
    {
        TestTool.test("TypeTrans", "d2x", "()V");
    }
}