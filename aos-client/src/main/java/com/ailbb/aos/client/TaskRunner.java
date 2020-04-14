package com.ailbb.aos.client;

/**
 * 这个工程中有一个类HelloClient类，这个类中有一个Hello数组类型的field，
 * 这个field需要让iPOJO自动的注入，
 * 当有实现了Hello服务接口的组件实例被公布后，iPOJO会自动的把实例添加到field中。
 * Created by Wz on 4/18/2019.
 */

import com.ailbb.aos.api.HelloService;
import org.osgi.util.tracker.ServiceTracker;

import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * HelloService Service simple client.
 *
 * @author <a href="mailto:dev@felix.apache.org">Felix Project Team</a>
 */
public class TaskRunner implements Runnable {
    private ServiceTracker serviceTracker;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    public TaskRunner(ExecutorService executorService, ServiceTracker serviceTracker) {
        this.executorService = executorService;
        this.serviceTracker = serviceTracker;
    }

    /**
     * Run method.
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        HelloService helloService = (HelloService) serviceTracker.getService(); // 获取服务

        if(null != helloService)
            helloService.sayHello(new Date().toString()); // 调用服务
        else
            System.out.println(">>> [client] not found anything bundle.");
    }
}