package sissel.classinfo;

import java.util.NoSuchElementException;

/**
 * 常量池的项的名称
 * Created by Sissel on 2016/7/25.
 */
public enum EConstPoolItem
{
    UTF8_INFO(1, -1),
    INTEGER_INFO(3, 5),
    FLOAT_INFO(4, 5),
    LONG_INFO(5, 9),
    DOUBLE_INFO(6, 9),
    CLASS_INFO(7, 3),
    STRING_INFO(8, 3),
    FIELD_REF_INFO(9, 5),
    METHOD_REF_INFO(10, 5),
    INTERFACE_REF_INFO(11, 5),
    NAME_TYPE_INFO(12, 5),
    METHOD_HANDLE_INFO(15, 4),
    METHOD_TYPE_INFO(16, 3),
    INVOKE_DYNAMIC_INFO(18, 5);

    public int tag;

    // 表示该项占几个字节，变长的项，值为-1
    public int count;

    private EConstPoolItem(int tag, int count)
    {
        this.tag = tag;
        this.count = count;
    }

    public static EConstPoolItem getByTag(int tag)
    {
        EConstPoolItem[] items = EConstPoolItem.values();
        for (EConstPoolItem item : items)
        {
            if (item.tag == tag)
            {
                return item;
            }
        }

        throw new NoSuchElementException("tag: " + tag);
    }
}
