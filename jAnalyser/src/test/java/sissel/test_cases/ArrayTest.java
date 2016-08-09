package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

/**
 * 数组的指令
 * Created by Sissel on 2016/8/5.
 */
public class ArrayTest
{

    @Test
    public void testNewArray() throws Exception
    {
        TestTool.test("Array", "newArray", "()I");
    }

    @Test
    public void testNewClassArray() throws Exception
    {
        TestTool.test("Array", "newClassArray", "()I");
    }

    @Test
    public void testMultiDimensionArray() throws Exception
    {
        TestTool.test("Array", "multiDimensionArray", "()I");
    }
}