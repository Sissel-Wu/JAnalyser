package sissel.classinfo;

import util.ByteTool;

/**
 * 方法的信息
 * Created by Sissel on 2016/7/26.
 */
public class MethodInfo extends FMInfo
{
    int max_stack;
    int max_locals;
    int code_length;
    byte[] code;
    int exception_table_length;
    ExceptionInfo[] exception_info_table;

    int method_attr_count;
    AttributeInfo[] method_attr;

    private void analyzeAttributes(byte[] codeAttrInfo, int attributesOffset)
    {
        method_attr_count = ByteTool.bigEnd(codeAttrInfo[attributesOffset], codeAttrInfo[attributesOffset + 1]);
        method_attr = new AttributeInfo[method_attr_count];

        AttributeInfo.analyzeHelp(classBinary, codeAttrInfo, attributesOffset, method_attr);
    }

    public MethodInfo(ClassBinary classBinary, int access_flags, String name, String descriptor,
                     int attributes_count, AttributeInfo[] attributes)
    {
        super(classBinary,access_flags, name, descriptor, attributes_count, attributes);

        AttributeInfo codeAttribute = getCodeAttribute();
        if (codeAttribute != null)
        {
            byte[] info = codeAttribute.info;

            max_stack = ByteTool.bigEnd(info[0], info[1]);
            max_locals = ByteTool.bigEnd(info[2], info[3]);

            code_length = ByteTool.bigEnd(info[4], info[5], info[6], info[7]);
            code = new byte[code_length];
            System.arraycopy(info, 8, code, 0, code_length);

            exception_table_length = ByteTool.bigEnd(info[8 + code_length], info[9 + code_length]);
            exception_info_table = new ExceptionInfo[exception_table_length];
            int current = 10 + code_length;
            for (int i = 0; i < exception_table_length; i++)
            {
                int start_pc = ByteTool.bigEnd(info[current], info[current + 1]);
                int end_pc = ByteTool.bigEnd(info[current + 2], info[current + 3]);
                int handler_pc = ByteTool.bigEnd(info[current + 4], info[current + 5]);

                int catch_type_index = ByteTool.bigEnd(info[current + 6], info[current + 7]);
                String catch_type = classBinary.extractStrFromClassInfo(catch_type_index);

                ExceptionInfo exceptionInfo = new ExceptionInfo(start_pc, end_pc, handler_pc, catch_type);
                exception_info_table[i] = exceptionInfo;

                current += 8;
            }

            analyzeAttributes(info, current);
        }
    }

    public AttributeInfo getCodeAttribute()
    {
        for (AttributeInfo attribute : attributes)
        {
            if ("Code".equals(attribute.name))
            {
                return attribute;
            }
        }
        return null;
    }
}
