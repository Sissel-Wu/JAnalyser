package sissel.classinfo;

/**
 * 表示运行时常量池里的符号引用
 * Created by Sissel on 2016/8/8.
 */
public class MethodRef
{
    public String className;
    public String descriptor;
    public String methodName;

    public MethodRef(String className, String descriptor, String methodName)
    {
        this.className = className;
        this.descriptor = descriptor;
        this.methodName = methodName;
    }
}
