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

    @Test
    public void testNewComplex() throws Exception
    {
        TestTool.test("ObjectOp", "newComplex", "()V");
    }

    @Test
    public void testNewSuperComplex() throws Exception
    {
        TestTool.test("ObjectOp", "newSuperComplex", "()V");
    }
}