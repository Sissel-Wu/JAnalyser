package util;

import static org.junit.Assert.*;

/**
 * 工具类的测试
 * Created by Sissel on 2016/7/25.
 */
public class ByteToolTest
{

    @org.junit.Test
    public void testBigEnd() throws Exception
    {
        assertEquals(257, ByteTool.bigEnd((byte)1, (byte)1));
        assertEquals(255 * 256, ByteTool.bigEnd((byte)-1, (byte)0));
    }
}