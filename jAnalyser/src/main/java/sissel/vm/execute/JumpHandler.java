package sissel.vm.execute;

import sissel.util.ByteTool;
import sissel.vm.EInstruction;
import sissel.vm.MyStackFrame;
import sissel.vm.ObjectInstance;

/**
 * 跳转指令
 * Created by Sissel on 2016/8/6.
 */
public class JumpHandler
{
    private static boolean[][] branchTable;
    static
    {
        branchTable = new boolean[3][6];
        branchTable[0] = new boolean[]{true, false, false, true, false, true}; // eq, ne, lt, ge, gt, le / ==
        branchTable[1] = new boolean[]{false, true, true, false, false, true}; // eq, ne, lt, ge, gt, le / <
        branchTable[2] = new boolean[]{false, true, false, true, true, false}; // eq, ne, lt, ge, gt, le / >
    }

    public static int iBranch(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int val1, val2;
        int x, y;

        if (instruction.byteCode < 0x9f)
        {
            y = instruction.byteCode - 0x99;
            val1 = (Integer) stackFrame.popStack();
            val2 = 0;
        }
        else
        {
            y = instruction.byteCode - 0x9f;
            val2 = (Integer) stackFrame.popStack();
            val1 = (Integer) stackFrame.popStack();
        }

        if (val1 == val2)
        {
            x = 0;
        }
        else if (val1 < val2)
        {
            x = 1;
        }
        else
        {
            x = 2;
        }

        if (branchTable[x][y])
        {
            return ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
        }
        else
        {
            return pc + 1;
        }
    }

    public static int aBranch(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        switch (instruction)
        {
            case if_acmpeq:
            case if_acmpne:
                Object o2 = stackFrame.popStack();
                Object o1 = stackFrame.popStack();
                if ((o1 == o2 && instruction == EInstruction.if_acmpeq) || (o1 != o2 && instruction == EInstruction.if_acmpne))
                {
                    return ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
                }
                else
                {
                    return pc + 1;
                }
            case ifnonnull:
            case ifnull:
                Object o = stackFrame.popStack();
                if ((o == null && instruction == EInstruction.ifnull) || (o != null && instruction == EInstruction.ifnonnull))
                {
                    return ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
                }
                else
                {
                    return pc + 1;
                }
        }

        throw new IndexOutOfBoundsException();
    }

    public static int go(EInstruction instruction, byte[] byteCodes, int pc)
    {
        switch (instruction)
        {
            case ggoto:
                return ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
            case goto_w:
                return ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2], byteCodes[pc + 3], byteCodes[pc + 4]);
        }

        throw new IndexOutOfBoundsException();
    }

    public static int jsr(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int returnAddress = -1;
        switch (instruction)
        {
            case jsr:
                returnAddress = ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2]);
                break;
            case jsr_w:
                returnAddress = ByteTool.bigEnd(byteCodes[pc + 1], byteCodes[pc + 2], byteCodes[pc + 3], byteCodes[pc + 4]);
                break;
        }

        stackFrame.pushStack(returnAddress);
        return 3;
    }

    public static int ret(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int returnAddress = -1;
        switch (instruction)
        {
            case ret:
                int index = ByteTool.uBigEnd(byteCodes[pc + 1]);
                returnAddress = (Integer) stackFrame.getLocal(index);
                break;
            case wide:
                // TODO: 2016/8/6
                break;
        }

        return returnAddress;
    }

    public static int tableSwitch(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int beginOffset = pc % 4 + pc;

        int defaultAddress = ByteTool.gen32(byteCodes, beginOffset);
        int low = ByteTool.gen32(byteCodes, beginOffset + 4);
        int high = ByteTool.gen32(byteCodes, beginOffset + 8);

        int count = high - low + 1;
        int[] offsets = new int[count];
        int start = beginOffset + 12;
        for (int i = 0; i < count; i++)
        {
            offsets[i] = ByteTool.gen32(byteCodes, start + i * 4);
        }

        int target = (Integer)stackFrame.popStack();
        try
        {
            return offsets[target - low];
        }
        catch (IndexOutOfBoundsException e)
        {
            return defaultAddress;
        }
    }

    public static int lookupSwitch(MyStackFrame stackFrame, EInstruction instruction, byte[] byteCodes, int pc)
    {
        int beginOffset = pc % 4 + pc;
        int pairs = ByteTool.gen32(byteCodes, beginOffset + 4);

        int target = (Integer)stackFrame.popStack();

        int endOffset = beginOffset + 8 + pairs * 8;
        for (int i = beginOffset + 8; i < endOffset; i += 8)
        {
            int match = ByteTool.gen32(byteCodes, i);
            if (target == match)
            {
                return ByteTool.gen32(byteCodes, i + 4);
            }
        }

        // default address
        return ByteTool.gen32(byteCodes, beginOffset);
    }
}
