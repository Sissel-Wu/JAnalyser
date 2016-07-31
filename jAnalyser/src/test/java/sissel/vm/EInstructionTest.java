package sissel.vm;

import static org.junit.Assert.*;

/**
 * 测试有无重复
 * Created by Sissel on 2016/7/30.
 */
public class EInstructionTest
{
    @org.junit.Test
    public void testDuplicate()
    {
        EInstruction[] all = EInstruction.values();
        assertEquals(205, all.length);
    }
}