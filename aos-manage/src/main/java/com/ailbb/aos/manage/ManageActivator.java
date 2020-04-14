package com.ailbb.aos.manage;

import com.ailbb.aos.api.HelloService;
import org.osgi.framework.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Wz on 4/23/2019.
 */
public class ManageActivator implements BundleActivator, ServiceListener {
    // List of available dictionary service references.
    private List<ServiceReference> m_refList = new ArrayList<>();
    // Maps service references to service objects.
    private Map<ServiceReference, HelloService> m_refToObjMap = new HashMap<>();

    private ScheduledExecutorService scheduledThreadPool;
    private ActivatorEvent activatorEvent;

    /**
     * 开始事件
     * @param context
     * @throws Exception
     */
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println(">>> [manage] start bundle");

        // 添加监听事件
        context.addServiceListener(this, String.format("(&(objectClass=%s)(helloService=*))", HelloService.class.getName()));

//             获取匹配的service
        ServiceReference[] refs = context.getServiceReferences(HelloService.class.getName(), "(helloService=*)");

        if(null == refs)  return;

        activatorEvent = new ActivatorEvent(context, m_refList, m_refToObjMap);
        activatorEvent.onStart(refs);

        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate( // 周期线程池
                new TaskRunner(scheduledThreadPool, context), // 执行线程
                1, // 延迟1秒
                10, // 每隔20秒执行一次
                TimeUnit.SECONDS // 执行周期粒度
        );
    }

    /**
     * 停止事件
     * @param context
     * @throws Exception
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println(">>> [manage] stop bundle");

        scheduledThreadPool.shutdown(); // 平滑的关闭ExecutorService，当此方法被调用时，ExecutorService停止接收新的任务并且等待已经提交的任务（包含提交正在执行和提交未执行）执行完成。当所有提交任务执行完毕，线程池即被关闭。
        if (!scheduledThreadPool.awaitTermination(5, TimeUnit.SECONDS)) // 当线程超时后还没有关闭，则强制关闭
            scheduledThreadPool.shutdownNow();
    }

    /**
     * 状态改变事件
     * @param event
     */
    @Override
    public void serviceChanged(ServiceEvent event) {
        System.out.println(">>> [manage] changed bundle");

        ServiceReference ref = event.getServiceReference();

        switch (event.getType()) {
            case ServiceEvent.REGISTERED: activatorEvent.onRegistered(ref);
                break;
            case ServiceEvent.UNREGISTERING: activatorEvent.onUnregistering(ref);
                break;
            case ServiceEvent.MODIFIED: activatorEvent.onModified(ref);
                break;
        }
    }

}