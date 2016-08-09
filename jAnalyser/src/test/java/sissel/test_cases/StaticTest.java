package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

/**
 * getstatic putstatic
 * Created by Sissel on 2016/8/6.
 */
public class StaticTest
{

    @Test
    public void testGetStatic() throws Exception
    {
        TestTool.test("Static", "getStatic", "()V");
    }
}