package sissel;

import com.sun.jdi.VirtualMachine;
import com.sun.jdi.event.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 捕获VM运行时的信息
 * Created by Sissel on 2016/7/14.
 */
public class Tracer extends Thread
{
    private VirtualMachine virtualMachine;
    private List<String> filterClasses;
    private boolean vmExit;

    public Tracer(VirtualMachine virtualMachine)
    {
        super("Tracer(" + virtualMachine.name() + ")");
        System.out.println(this.getName() + " start!");

        this.vmExit = false;
        this.filterClasses = new LinkedList<>();
        this.virtualMachine = virtualMachine;
    }

    public void setFilterClasses(List<String> filterClasses)
    {
        this.filterClasses = filterClasses;
    }

    private void handleEvent(Event event)
    {
        if (event instanceof VMDisconnectEvent)
        {
            onVMDisconnected((VMDisconnectEvent) event);
        }
    }

    @Override
    public void run()
    {
        EventQueue eventQueue = virtualMachine.eventQueue();
        while (!vmExit)
        {
            try
            {
                EventSet eventSet = eventQueue.remove();
                EventIterator eventIterator = eventSet.eventIterator();
                while (eventIterator.hasNext())
                {
                    handleEvent(eventIterator.nextEvent());
                    eventSet.resume();
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void onVMDisconnected(VMDisconnectEvent event)
    {
        System.out.println("===== VMDisconnected =====");
        vmExit = true;
    }
}
