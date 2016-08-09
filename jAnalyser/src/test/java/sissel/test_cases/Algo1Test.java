package sissel.test_cases;

import org.junit.Test;
import sissel.util.TestTool;

import static org.junit.Assert.*;

/**
 * 测试一整个算法
 * Created by Sissel on 2016/8/9.
 */
public class Algo1Test
{

    @Test
    public void testMain() throws Exception
    {
        TestTool.test("Algo1", "main", "([Ljava/lang/String;)V");
    }
}