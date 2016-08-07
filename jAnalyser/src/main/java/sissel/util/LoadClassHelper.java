package sissel.util;

import com.sun.jdi.ReferenceType;
import sissel.classinfo.ClassBinary;

import java.io.IOException;
import java.io.InputStream;

/**
 * 加载类 -> ClassBinary类
 * Created by Sissel on 2016/8/7.
 */
public class LoadClassHelper
{
    /**
     * 加载类（不做复制和初始化）
     * @param referenceType jdi里面反射出来的类
     * @return 根据referenceType名字寻找的解析好的ClassBinary
     */
    public final static ClassBinary load(ReferenceType referenceType)
    {
        return load(referenceType.name());
    }

    /**
     * 加载类（不做复制和初始化）
     * @param className 类名字（包之间用“.”分割，最后以类名结尾）
     * @return 解析好的ClassBinary
     */
    public final static ClassBinary load(String className)
    {
        java.lang.ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String resourceName = className.replaceAll("\\.", "/") + ".class";
        InputStream in = classLoader.getResourceAsStream(resourceName);
        if (in == null)
        {
            System.err.println("Class File Not found: " + className);
            return null;
        }
        else
        {
            try
            {
                return new ClassBinary(in);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }
    }
}
