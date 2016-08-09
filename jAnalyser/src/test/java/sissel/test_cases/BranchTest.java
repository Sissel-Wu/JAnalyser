package sissel.test_cases;

import org.junit.Test;
import org.junit.runners.model.TestTimedOutException;
import sissel.util.TestTool;

import static org.junit.Assert.*;

/**
 * 分支跳转指令
 * Created by Sissel on 2016/8/9.
 */
public class BranchTest
{

    @Test
    public void testLbranch() throws Exception
    {
        assert (Integer)TestTool.test("Branch", "lbranch", "()I") == 123;
    }

    @Test
    public void testZbranch() throws Exception
    {
        assert (Integer)TestTool.test("Branch", "zbranch", "()I") == 12;
    }

    @Test
    public void testIbranch() throws Exception
    {
        assert  (Integer)TestTool.test("Branch", "ibranch", "()I") == 2;
    }

    @Test
    public void testAbranch() throws Exception
    {
        assert (Integer) TestTool.test("Branch", "abranch", "()I") == 1;
    }

    @Test
    public void testTableSwitch() throws Exception
    {
        assert (Integer)TestTool.test("Branch", "tableSwitch", "()I") == 2;
    }

    @Test
    public void testLookupSwitch() throws Exception
    {
        assert (Integer)TestTool.test("Branch", "lookupSwitch", "()I") == 2;
    }
}