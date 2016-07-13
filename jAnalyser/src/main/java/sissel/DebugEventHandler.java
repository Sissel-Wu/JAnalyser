package sissel;

import com.sun.jdi.event.Event;

/**
 * 定义一个函数接口
 * Created by Sissel on 2016/7/14.
 */
@FunctionalInterface
public interface DebugEventHandler
{
    void handle(Event event);
}
