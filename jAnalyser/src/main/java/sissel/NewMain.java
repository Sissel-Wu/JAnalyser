package sissel;

import com.sun.jdi.Bootstrap;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.VirtualMachineManager;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.VMStartException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 重构后结构好一些的Main
 * Created by Sissel on 2016/7/14.
 */
public class NewMain
{
    /**
     * 启动虚拟机，分发任务
     */
    public static void main(String[] args)
    {
        // TODO: 2016/7/14 替换成参数
        String mainClass = "sissel.HelloWorld";
        String options = "-cp G:\\Repository\\JAnalyser\\jAnalyser\\target\\classes";
        List<String> filterClassses = new LinkedList<>();
        filterClassses.add("sissel.HelloWorld");

        VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
        LaunchingConnector connector = vmManager.defaultConnector();

        Map<String, Connector.Argument> argumentMap = connector.defaultArguments();
        argumentMap.get("main").setValue(mainClass);
        argumentMap.get("options").setValue(options);
        argumentMap.get("suspend").setValue("true");

        try
        {
            VirtualMachine vm = connector.launch(argumentMap);

            //vm.setDebugTraceMode(1);
            Tracer tracer = new Tracer(vm);
            tracer.setFilterClasses(filterClassses);
            tracer.start();
            vm.resume();

            // TODO: 2016/7/14 redirect
            Process process = vm.process();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), "gbk"));
            br.lines().forEach(System.out::println);

            br = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gbk"));
            br.lines().forEach(System.err::println);

            process.destroy();
        }
        // TODO: 2016/7/14 异常处理
        catch (VMStartException | IllegalConnectorArgumentsException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
