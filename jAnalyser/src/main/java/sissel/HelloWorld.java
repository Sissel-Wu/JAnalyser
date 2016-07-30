package sissel;

import java.io.Serializable;
import java.util.Scanner;

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

    public static void main(String[] args)
    {
        int a = 12;
        byte b = 1;
        long l = 123141412312L;
        short s = 1234;
        char c = 'c';

        boolean bl = false;

        double d = 1.234;
        float f = 2.442f;

        String hello = "hello, world!";
        String goodbye = "goodbye";

        double[] ds = new double[]{1.2, 2.33, 3.5, 8.7};

        HelloWorld hw = new HelloWorld();

        System.out.println(hello);
        System.out.println(goodbye);

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    @Override
    public String toString()
    {
        return "hello, " + val;
    }
}
