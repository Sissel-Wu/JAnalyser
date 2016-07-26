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

    @org.junit.Test
    public void testGenFlags()
    {
        assertEquals(0x0411, ByteTool.genFlags((byte)4, (byte)17));
        assertEquals(0x2011, ByteTool.genFlags((byte)32, (byte)17));
        assertEquals((short)0xa011, (short)ByteTool.genFlags((byte)160, (byte)17));
    }
}