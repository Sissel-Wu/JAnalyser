package sissel.experiment;

/**
 * static 有关的java规定
 * Created by Sissel on 2016/8/8.
 */
public class AboutStatic
{
    // 结论：同名的field不覆盖，每个类分别存
    static void whereStaticStore()
    {
        Son1.val = 2;
        Father1.val = 3;
        System.out.println(Father1.val);
        System.out.println(Son1.val);
    }


    static void whichStaticMethodCall()
    {
        Father1 son = new Father1();
        son.func();
    }

    public static void main(String[] args)
    {
        whereStaticStore();
        whichStaticMethodCall();
    }
}

class Father1
{
    static int val;
    static
    {
        System.out.println("Father1 <clinit>");
    }

    public static void func()
    {
        System.out.println("call Father");
    }
}

class Son1 extends Father1
{
    static int val;
    static
    {
        System.out.println("Son1 <clinit>");
    }

    public static void func()
    {
        System.out.println("call Son");
    }
}