package sissel.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 帮助生成字节码指令的枚举用的
 * Created by Sissel on 2016/7/30.
 */
public class InstructionConsole
{
    private final static char[] charTable = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static String iTo0x(int i)
    {
        int head = i / 16;
        int tail = i % 16;
        return "0x" + charTable[head] + charTable[tail];
    }

    public static void main(String[] args)
    {
        List<String> result = new LinkedList<>();
        Scanner scanner = new Scanner(System.in);

        String line;
        int count = 0;
        while (!((line = scanner.nextLine()).equals("")))
        {
            String byteCodeName;
            if (line.contains(" "))
            {
                String[] strs = line.split(" ");
                byteCodeName = strs[0];
                String temp = strs[1];
                count = Integer.parseInt(temp);
            }
            else
            {
                byteCodeName = line;
            }

            String num_str = iTo0x(count);
            ++count;
            result.add(byteCodeName + "(" + num_str + "), ");
        }

        result.stream().forEach(System.out::println);
    }
}
