package sissel.classinfo;

/**
 * 字段的信息
 * Created by Sissel on 2016/7/26.
 */
public class FieldInfo extends FMInfo
{
    public Object value;

    public FieldInfo(ClassBinary classBinary, int access_flags, String name, String descriptor,
                     int attributes_count, AttributeInfo[] attributes)
    {
        super(classBinary,access_flags, name, descriptor, attributes_count, attributes);
    }
}