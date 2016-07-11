package sissel;

import com.sun.jdi.*;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.*;
import com.sun.jdi.request.BreakpointRequest;
import com.sun.jdi.request.ClassPrepareRequest;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.EventRequestManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by Sissel on 2016/7/10.
 */

public class Main
{
    private static VirtualMachine vm;
    private static Process process;
    private static EventRequestManager eventRequestManager;
    private static EventQueue eventQueue;
    private static EventSet eventSet;
    private static boolean vmExit = false;

    public static void main(String[] args) throws Exception
    {
        VirtualMachineManager machineManager = Bootstrap.virtualMachineManager();
        LaunchingConnector defaultConnector = machineManager.defaultConnector();

        Map<String, Connector.Argument> defaultArguments = defaultConnector.defaultArguments();
        Connector.Argument mainArg = defaultArguments.get("main");
        Connector.Argument suspendArg = defaultArguments.get("suspend");
        Connector.Argument options = defaultArguments.get("options");

        suspendArg.setValue("true");
        mainArg.setValue("sissel.HelloWorld");
        options.setValue("-cp G:\\Repository\\JAnalyser\\jAnalyser\\target\\classes");

        System.out.println(options.description());

        vm = defaultConnector.launch(defaultArguments);

        process = vm.process();

        eventRequestManager = vm.eventRequestManager();
        ClassPrepareRequest classPrepareRequest = eventRequestManager.createClassPrepareRequest();
        classPrepareRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
        classPrepareRequest.enable();

        eventLoop();

        InputStreamReader reader = new InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(reader);

        br.lines().forEach(System.out::println);

        br = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gbk"));
        br.lines().forEach(System.err::println);

        process.destroy();
    }

    private static void eventLoop() throws Exception
    {
        eventQueue = vm.eventQueue();
        while (!vmExit)
        {
            eventSet = eventQueue.remove();
            EventIterator eventIterator = eventSet.eventIterator();
            while (eventIterator.hasNext())
            {
                Event event = eventIterator.nextEvent();
                execute(event);
            }
        }
    }

    private static void execute(Event event) throws AbsentInformationException, IncompatibleThreadStateException
    {
        if (event instanceof VMStartEvent)
        {
            System.out.println("===== VM started =====");
            eventSet.resume();
        }
        else if (event instanceof ClassPrepareEvent)
        {
            ClassPrepareEvent classPrepareEvent = (ClassPrepareEvent) event;
            String mainClassName = classPrepareEvent.referenceType().name();
            if (mainClassName.contains("HelloWorld")) {
                System.out.println("Class " + mainClassName
                        + " is already prepared");

                // Get location
                ReferenceType referenceType = classPrepareEvent.referenceType();
                List locations = referenceType.locationsOfLine(14);
                Location location = (Location) locations.get(0);

                // Create BreakpointEvent
                BreakpointRequest breakpointRequest = eventRequestManager
                        .createBreakpointRequest(location);
                breakpointRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
                breakpointRequest.enable();
            }
            eventSet.resume();

        }
        else if (event instanceof BreakpointEvent)
        {
            System.out.println("Reach line 14 of sissel.HelloWorld");
            BreakpointEvent breakpointEvent = (BreakpointEvent) event;
            ThreadReference threadReference = breakpointEvent.thread();
            StackFrame stackFrame = threadReference.frame(0);
            LocalVariable localVariable = stackFrame
                    .visibleVariableByName("hello");
            Value value = stackFrame.getValue(localVariable);
            String str = ((StringReference) value).value();
            System.out.println("The local variable str at line 14 is " + str
                    + " of " + value.type().name());
            eventSet.resume();
        }
        else if (event instanceof VMDisconnectEvent)
        {
            System.out.println("===== VMDisconnected =====");
            vmExit = true;
        }
        else
        {
            eventSet.resume();
        }
    }
}
