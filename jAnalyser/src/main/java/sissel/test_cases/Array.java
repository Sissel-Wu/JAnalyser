package sissel.test_cases;

/**
 * 数组
 * Created by Sissel on 2016/8/5.
 */
public class Array
{
    public int newArray()
    {
        int[] arr = new int[10];

        arr[1] = 2;
        arr[2] = 4;

        int result = arr[1] + arr[2];

        return result;
    }

    public int newClassArray()
    {
        Simple[] simples = new Simple[4];

        return simples.length;
    }

    public int multiDimensionArray()
    {
        int[][] arr = new int[10][19];

        return arr[1][0];
    }
}
