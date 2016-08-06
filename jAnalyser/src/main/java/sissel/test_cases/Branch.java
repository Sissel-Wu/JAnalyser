package sissel.test_cases;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * 分支的指令
 * Created by Sissel on 2016/8/6.
 */
public class Branch
{
    public int lbranch()
    {
        long l1 = 233424242424L, l2 = 23132142421341421L;

        if (l1 < l2)
        {
            return 123;
        }
        else
        {
            return 21;
        }
    }

    public int zbranch()
    {
        int a = 1;
        if (a > 0)
        {
            return 12;
        }
        else
        {
            return 123;
        }
    }

    public int ibranch()
    {
        int a = 1, b = 2;
        if (a > b)
        {
            return a;
        }
        else
        {
            return b;
        }
    }
    
    public int abranch()
    {
        // TODO: 2016/8/6  
        throw new NotImplementedException();
    }

    public int tableSwitch()
    {
        int a = 2;
        a--;
        switch (a)
        {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }
}
