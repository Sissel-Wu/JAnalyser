package sissel.util;

import sissel.vm.EInstruction;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * Created by Sissel on 2016/8/1.
 */
public class ByteCodeMaker
{
    private List<Byte> byteCodes = new LinkedList<>();

    public void add(EInstruction instruction, byte...follow)
    {
        byteCodes.add((byte)instruction.byteCode);
        for (byte b : follow)
        {
            byteCodes.add(b);
        }
    }

    public byte[] getByteCodes()
    {
        byte[] result = new byte[byteCodes.size()];

        Iterator<Byte> iterator = byteCodes.iterator();
        for (int i = 0; i < result.length; i++)
        {
            result[i] = byteCodes.get(iterator.next());
        }

        return result;
    }
}
