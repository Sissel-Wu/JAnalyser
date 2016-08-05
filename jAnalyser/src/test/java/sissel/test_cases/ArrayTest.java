package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

import static org.junit.Assert.*;

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
}