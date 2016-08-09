package sissel;

import java.io.Serializable;

/**
 * demo to be tested on
 * Created by Sissel on 2016/7/10.
 */

class Father
{
    protected int val = 12;
}

public class HelloWorld extends Father implements Serializable
{
    protected int val = 123;
    protected String name = "hellsing";
    private static int id = 12450;

    public static void main(String[] args)
    {
        HelloWorld hw1 = new HelloWorld();
        HelloWorld hw2 = new HelloWorld();
        SeparateTest t1 = new SeparateTest();
        hw1.val = 1;
        hw2.val = 2;

        System.out.println("hello");
        System.out.println(", world");
    }

    @Override
    public String toString()
    {
        return "hello, " + val;
    }
}
