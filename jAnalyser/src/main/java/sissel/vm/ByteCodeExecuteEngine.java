package sissel.vm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 每个线程有一个，执行字节码
 * Created by Sissel on 2016/7/28.
 */
public class ByteCodeExecuteEngine extends Thread
{
    List<ThreadCopy> threads;

    public ByteCodeExecuteEngine(List<ThreadCopy> threads)
    {
        this.threads = threads;
    }

    public ByteCodeExecuteEngine(ThreadCopy...threads)
    {
        this.threads = new ArrayList<>(threads.length * 2);
        Collections.addAll(this.threads, threads);
    }

    @Override
    public void run()
    {

    }
}
