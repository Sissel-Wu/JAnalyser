package sissel.util;

import com.sun.jdi.*;

import java.util.List;

/**
 * 将jdi的StackFrame转成String显示
 * Created by Sissel on 2016/7/18.
 */
public class DisplayString
{
    public enum SpecLevel
    {
        MAX,
        MEDIUM,
        MIN
    }

    private final static String singleIndent = "    "; // 4 spaces

    private List<String> filterClasses;

    public DisplayString(List<String> filterClasses)
    {
        this.filterClasses = filterClasses;
    }

    private static String indents(int indentCount)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indentCount; i++)
        {
            sb.append(singleIndent);
        }
        return sb.toString();
    }

    /**
     *
     * @param stackFrame stackFrame that contains the local variable
     * @param localVariable the local variable to display
     * @param n indents
     * @return the human-readable display of the local variable
     */
    public String translate(StackFrame stackFrame, LocalVariable localVariable, int n, SpecLevel specLevel)
    {
        StringBuilder sb = new StringBuilder();
        // name and type
        sb.append(indents(n));
        sb.append(localVariable.name());
        sb.append("(").append(localVariable.typeName()).append(")");

        sb.append(":");

        // value
        Value value = stackFrame.getValue(localVariable);
        sb.append(value);
        sb.append("\n");

        // later dependant on specLevel
        try
        {
            Type type = localVariable.type();
            if (type instanceof ClassType && filterClasses.contains(type.name()))
            {
                sb.append("todo display classType\n");
                switch (specLevel)
                {
                    case MAX: // recursively
                        break;
                    case MEDIUM: // partly display
                        break;
                    case MIN: // do nothing
                        break;
                }
            }

            if (type instanceof ArrayType)
            {
                sb.append("todo display arrayType\n");
                switch (specLevel)
                {
                    case MAX: // recursively
                        break;
                    case MEDIUM: // partly display
                        break;
                    case MIN: // do nothing
                        break;
                }
            }
        }
        // TODO: 2016/7/19 unhandled
        catch (ClassNotLoadedException e)
        {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
