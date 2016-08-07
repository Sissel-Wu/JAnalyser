package sissel.classinfo;

/**
 * 表示运行时常量池里的符号引用
 * Created by Sissel on 2016/8/6.
 */
public class FieldRef
{
    public String className;
    public String descriptor;
    public String fieldName;

    public FieldRef(String className, String descriptor, String fieldName)
    {
        this.className = className;
        this.descriptor = descriptor;
        this.fieldName = fieldName;
    }
}
