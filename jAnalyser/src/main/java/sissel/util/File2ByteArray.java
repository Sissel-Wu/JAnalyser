package sissel.util;

import java.io.*;

/**
 * 将文件读取成字节数组的工具类
 * Created by Sissel on 2016/7/25.
 */
public class File2ByteArray
{
    public static byte[] read(String filePath) throws IOException
    {
        File file = new File(filePath);
        if (!file.exists())
        {
            throw new FileNotFoundException(filePath);
        }

        if (file.length() < Integer.MAX_VALUE)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            final int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int n;
            while ((n = inputStream.read(buffer)) != -1)
            {
                bos.write(buffer, 0, n);
            }

            return bos.toByteArray();
        }
        else
        {
            // TODO: 2016/7/25 handle big file
            throw new ArrayIndexOutOfBoundsException();
        }
    }
}
