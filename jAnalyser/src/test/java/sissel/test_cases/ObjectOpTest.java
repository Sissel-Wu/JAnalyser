package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

import static org.junit.Assert.*;

/**
 * 对象有关的指令
 * Created by Sissel on 2016/8/7.
 */
public class ObjectOpTest
{

    @Test
    public void testNewSimple() throws Exception
    {
        TestTool.test("ObjectOp", "newSimple", "()V");
    }
}