package sissel.classinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * IClass的桩，测试用
 * Created by Sissel on 2016/8/1.
 */
public class ClassBinaryStub implements IClass
{
    public List<Object> cpList = new ArrayList<>();

    @Override
    public Object getItem(int index)
    {
        return cpList.get(index);
    }
}
