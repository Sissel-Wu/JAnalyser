package sissel.vm.execute;

import sissel.classinfo.ClassBinary;
import sissel.classinfo.MethodInfo;
import sissel.util.ByteTool;
import sissel.vm.EInstruction;
import sissel.vm.HeapDump;
import sissel.vm.MyStackFrame;
import sissel.vm.ThreadCopy;

/**
 * scope: 每个线程的执行
 * Created by Sissel on 2016/7/31.
 */
public class ThreadExecutor
{
    private ThreadCopy thread;
    private HeapDump heap;

    public ThreadExecutor(ThreadCopy thread)
    {
        this.thread = thread;
        this.heap = thread.heap;
    }

    public void start()
    {
        thread.state = ThreadCopy.ThreadState.RUN;

        runCurrentMethod();
    }

    public Object runCurrentMethod()
    {
        MyStackFrame stackFrame = thread.currentStackFrame();
        MethodInfo methodInfo = stackFrame.getMethodInfo();
        ClassBinary classBinary = stackFrame.getMethodInfo().classBinary;

        int codeLength = methodInfo.code_length;
        byte[] byteCodes = methodInfo.code;
        int pc = 0;
        while (pc < codeLength)
        {
            EInstruction instruction = EInstruction.forr(byteCodes[pc]);
            int byteCode = instruction.byteCode;
            if (byteCode > 0x00 && byteCode <= 0x0f) // xconst
            {
                pc += ConstHandler.pushInstant(stackFrame, instruction);
            }
            else if (byteCode >= 0x10 && byteCode <= 0x11) // bipush, sipush
            {
                pc += ConstHandler.pushLaterInstant(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0x12 && byteCode <= 0x14) // ldc, ldc_w, ldc2_w
            {
                pc += ConstHandler.pushConstant(classBinary, stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0x15 && byteCode <= 0x19) // xload index
            {
                ConstHandler.pushLocal(stackFrame, instruction, ByteTool.uBigEnd(byteCodes[pc + 1]));
                pc += 2;
            }
            else if (byteCode >= 0x1a && byteCode <= 0x2d) // xload_<n>
            {
                pc += ConstHandler.pushLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x2e && byteCode <= 0x35) // xaload
            {
                pc += ConstHandler.pushArrayLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x36 && byteCode <= 0x3a) // xstore index
            {
                ConstHandler.storeLocal(stackFrame, instruction, ByteTool.uBigEnd(byteCodes[pc + 1]));
                pc += 2;
            }
            else if (byteCode >= 0x3b && byteCode <= 0x4e) // xstore_<n>
            {
                pc += ConstHandler.storeLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x4f && byteCode <= 0x56) // xastore
            {
                pc += ConstHandler.storeArrayLocal(stackFrame, instruction);
            }
            else if (byteCode >= 0x57 && byteCode <= 0x5f) // stack_op pop,dup,swap
            {
                pc += ConstHandler.stackOp(stackFrame, instruction);
            }
            else if (byteCode >= 0x60 && byteCode <= 0x73) // x(add/sub/mul/div/rem)
            {
                pc += CalculateHandler.arithmetic(stackFrame, instruction);
            }
            else if (byteCode >= 0x74 && byteCode <= 0x77) // xneg
            {
                pc += CalculateHandler.neg(stackFrame, instruction);
            }
            else if (byteCode >= 0x78 && byteCode <= 0x83) // shl,shr,ushr,and.or,xor
            {
                pc += CalculateHandler.raw(stackFrame, instruction);
            }
            else if (byteCode == 0x84) // iinc
            {
                pc += CalculateHandler.iinc(stackFrame, ByteTool.uBigEnd(byteCodes[pc + 1]), byteCodes[pc + 2]);
            }
            else if (byteCode >= 0x85 && byteCode <= 0x93) // x2y
            {
                pc += CalculateHandler.transform(stackFrame, instruction);
            }
            else if (byteCode >= 0x94 && byteCode <= 0x98) // xcmp<lg>
            {
                pc += CalculateHandler.compare(stackFrame, instruction);
            }
            else if (byteCode >= 0x99 && byteCode <= 0xa4) // if, if_icmp
            {
                pc += JumpHandler.iBranch(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0xa5 && byteCode <= 0xa6) // if_acmp
            {
                pc += JumpHandler.aBranch(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode == 0xa7 || byteCode == 0xc8) // goto
            {
                pc = JumpHandler.go(instruction, byteCodes, pc);
            }
            else if (byteCode == 0xa8 || byteCode == 0xc9) // jsr
            {
                pc += JumpHandler.jsr(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode == 0xa9) // ret
            {
                pc = JumpHandler.ret(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode == 0xaa) // table switch
            {
                pc += JumpHandler.tableSwitch(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode == 0xab) // lookup switch
            {
                pc += JumpHandler.lookupSwitch(stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0xac && byteCode <= 0xb0) // xreturn
            {
                return stackFrame.popStack();
            }
            else if (byteCode == 0xb1) // return (void)
            {
                return null;
            }
            else if (byteCode >= 0xb2 && byteCode <= 0xb5) // get put (static/field)
            {
                pc += ObjectHandler.getPutSF(heap, thread, classBinary, stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0xb6 && byteCode <= 0xba)
            {
                pc += InvokeHandler.invoke(heap, thread, classBinary, stackFrame, instruction, byteCodes, pc);
            }
            else if (byteCode >= 0xbb && byteCode <= 0xbd) // new
            {
                pc += ObjectHandler.newObject(heap, thread, classBinary, stackFrame, instruction, byteCodes, pc);
            }
            else
            {
                switch (instruction)
                {
                    case arraylength:
                        pc += ObjectHandler.length(stackFrame);
                        break;
                    case ifnull:
                    case ifnonnull:
                        pc += JumpHandler.aBranch(stackFrame, instruction, byteCodes, pc);
                        break;
                }
            }
        }

        return null;
    }
}
