package sissel.test_cases;

/**
 * 测试算法：冒泡排序
 * Created by Sissel on 2016/8/9.
 */
public class Algo1
{
    public int[] sort(int[] origin)
    {
        for (int i = 0; i < origin.length; i++)
        {
            for (int j = origin.length - 1; j > i; j--)
            {
                if (origin[j] < origin[j - 1]) // swap
                {
                    int temp = origin[j];
                    origin[j] = origin[j - 1];
                    origin[j - 1] = temp;
                }
            }
        }

        return origin;
    }

    public static void main(String[] args)
    {
        Algo1 a = new Algo1();
        int[] arr = new int[]{23, 245, 12, 786, 2174, 147};

        int[] result = a.sort(arr);
    }
}
