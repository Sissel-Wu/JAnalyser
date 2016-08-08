package sissel.experiment;

/**
 * class文件中怎么表现覆盖
 * 结论：要覆盖的就包含，不覆盖的就没有
 * Created by Sissel on 2016/8/8.
 */
public class AboutOverride
{
    public static void main(String[] args)
    {
        Father2 son = new Son2();
        System.out.println(son.nonInherit());
    }
}

class Father2
{
    protected int nonInherit()
    {
        return 32;
    }

    protected int inherit()
    {
        return 1;
    }
}

class Son2 extends Father2
{
    @Override
    protected int inherit()
    {
        return 2;
    }
}