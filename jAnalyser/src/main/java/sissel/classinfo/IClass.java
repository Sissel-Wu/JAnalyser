package sissel.classinfo;

/**
 * 运行时类信息的接口
 * Created by Sissel on 2016/8/1.
 */
public interface IClass
{
    public Object getItem(int index);

    public MethodInfo getMethodInfo(String name, String descriptor);
}
