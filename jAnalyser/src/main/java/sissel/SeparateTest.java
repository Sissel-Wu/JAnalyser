package sissel;

/**
 * 看看能不能根据jdi找到这个类
 * Created by Sissel on 2016/8/7.
 */

class SeparateFather
{
    static int fid = 12;
    int count = 56;
}

public class SeparateTest extends SeparateFather
{
    static int sid = 34;
    int scount = 78;
}
