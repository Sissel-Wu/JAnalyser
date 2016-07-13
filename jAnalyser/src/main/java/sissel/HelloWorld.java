package sissel;

/**
 * demo to be tested on
 * Created by Sissel on 2016/7/10.
 */

class Father
{
    protected int val = 12;
}

public class HelloWorld extends Father
{
    protected int val = 123;

    public static void main(String[] args)
    {
        String hello = "hello, world!";
        String goodbye = "goodbye";
        System.out.println(hello);
        System.out.println(goodbye);
    }
}
