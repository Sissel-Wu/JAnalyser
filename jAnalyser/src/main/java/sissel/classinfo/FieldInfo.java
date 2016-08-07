package sissel.classinfo;

import sissel.util.FieldDefault;

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
        super(classBinary, access_flags, name, descriptor, attributes_count, attributes);
    }

    public boolean isVolatile()
    {
        return (access_flags & 0x0040) != 0;
    }

    public boolean isTransient()
    {
        return (access_flags & 0x0080) != 0;
    }

    public boolean isSynthetic()
    {
        return (access_flags & 0x1000) != 0;
    }

    public boolean isEnum()
    {
        return (access_flags & 0x4000) != 0;
    }

    // 初始化静态域成默认值
    public void initializeDefault()
    {
        value = FieldDefault.fromDescriptor(descriptor);
    }
}