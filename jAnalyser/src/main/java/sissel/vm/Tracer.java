package sissel.vm;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;
import sissel.util.DisplayString;

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
    private Map<String, List<Integer>> breakpointMap; // key is class name, value is the breakpoints
    private boolean vmExit;
    private EventHandler eventHandler;
    private DisplayString strHelper;

    public Tracer(VirtualMachine virtualMachine)
    {
        super("Tracer(" + virtualMachine.name() + ")");
        System.out.println(this.getName() + " start!");

        this.vmExit = false;
        this.eventHandler = new EventHandler();
        this.filterClasses = new LinkedList<>();
        this.virtualMachine = virtualMachine;
    }

    public void setFilterClasses(List<String> filterClasses)
    {
        this.filterClasses = filterClasses;
        this.strHelper = new DisplayString(filterClasses);
        this.setClassPrepareRequest();
    }

    public void setBreakpoints(Map<String, List<Integer>> map)
    {
        this.breakpointMap = map;
    }

    private void setClassPrepareRequest()
    {
        EventRequestManager erm = virtualMachine.eventRequestManager();
        ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
        filterClasses.forEach(classPrepareRequest::addClassFilter);
        classPrepareRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        classPrepareRequest.enable();
    }

    private void setClassBreakpointRequest(ReferenceType type, List<Integer> lines)
    {
        EventRequestManager eventRequestManager = virtualMachine.eventRequestManager();
        for (Integer line : lines)
        {
            try
            {
                Location location = type.locationsOfLine(line).get(0);
                BreakpointRequest breakpointRequest = eventRequestManager.createBreakpointRequest(location);
                breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
                breakpointRequest.enable();
            }
            catch (AbsentInformationException e)
            {
                e.printStackTrace();
            }
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
                    Event event = eventIterator.nextEvent();
                    eventHandler.handleEvent(event);
                    eventSet.resume();
                }
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class EventHandler
    {
        // 分发具体处理
        private void handleEvent(Event event)
        {
            if (event instanceof VMDisconnectEvent)
            {
                onVMDisconnected((VMDisconnectEvent) event);
            }
            else if (event instanceof VMDeathEvent)
            {
                onVMDeath((VMDeathEvent) event);
            }
            else if (event instanceof ClassPrepareEvent)
            {
                onClassPrepare((ClassPrepareEvent) event);
            }
            else if (event instanceof VMStartEvent)
            {
                onVMStartEvent((VMStartEvent)event);
            }
            else if (event instanceof BreakpointEvent)
            {
                onBreakpoint((BreakpointEvent)event);
            }
            else
            {
                System.out.println("===== event " + event.getClass().getSimpleName() + " not handle =====");
            }
        }

        private void onVMStartEvent(VMStartEvent event)
        {
            System.out.println("===== VMStart =====");
        }

        private void onVMDisconnected(VMDisconnectEvent event)
        {
            System.out.println("===== VMDisconnected =====");
            vmExit = true;
        }

        private void onVMDeath(VMDeathEvent event)
        {
            System.out.println("===== VMDeath =====");
        }

        private void onClassPrepare(ClassPrepareEvent event)
        {
            ReferenceType referenceType = event.referenceType();
            System.out.println("===== class " + referenceType.name() + " prepared =====");
            List<Integer> breakpoints = breakpointMap.get(referenceType.name());
            if (breakpoints != null)
            {
                setClassBreakpointRequest(referenceType, breakpoints);
            }
        }

        private void onBreakpoint(BreakpointEvent event)
        {
            Location location = event.location();
            System.out.println("===== breakpoint at " + location.method().name() + ": " + location.lineNumber() + " =====");

            try
            {
                ThreadReference threadReference = event.thread();
                List<StackFrame> frames = threadReference.frames();
                StackFrame currentFrame = frames.get(0);

                List<LocalVariable> visibleVars = currentFrame.visibleVariables();
                for (LocalVariable visibleVar : visibleVars)
                {
                    String info = strHelper.translate(currentFrame, visibleVar, 0, DisplayString.SpecLevel.MAX);
                    System.out.print(info);
                }
            }
            // TODO: 2016/7/16 handle exception
            catch (AbsentInformationException | IncompatibleThreadStateException e)
            {
                e.printStackTrace();
            }

            System.out.println("===== heap dump =====");
            HeapDump heapDump = HeapDump.newInstance(virtualMachine);
            System.out.println(heapDump.toString());
            heapDump.initialize();
            System.out.println("initialization over");

            Map<ObjectReference, ObjectInstance> checkee = heapDump.objMap.get("sissel.HelloWorld");
            System.out.println("final over");
        }
    }
}
