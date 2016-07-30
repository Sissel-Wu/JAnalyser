package sissel.classinfo;

/**
 * Code属性里面的
 * Created by Sissel on 2016/7/29.
 */
public class ExceptionInfo
{
    int start_pc;
    int end_pc;
    int handler_pc;
    String catch_type; // 若为null，等于规范里面的0(finally)

    public ExceptionInfo(int start_pc, int end_pc, int handler_pc, String catch_type)
    {
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }
}
