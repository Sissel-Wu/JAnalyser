package sissel.util;

import java.io.*;

/**
 * 将文件读取成字节数组的工具类
 * Created by Sissel on 2016/7/25.
 */
public class ByteArrayTool
{
    public static byte[] fromStream(InputStream in) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream inputStream = new BufferedInputStream(in);
        final int buf_size = 1024;
        byte[] buffer = new byte[buf_size];
        int n;
        while ((n = inputStream.read(buffer)) != -1)
        {
            bos.write(buffer, 0, n);
        }

        return bos.toByteArray();
    }

    public static byte[] fromFile(String filePath) throws IOException
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
