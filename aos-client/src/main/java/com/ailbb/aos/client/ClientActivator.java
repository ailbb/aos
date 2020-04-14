package com.ailbb.aos.client;

import com.ailbb.aos.api.HelloService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import java.util.concurrent.*;

/**
 * Created by Wz on 4/23/2019.
 */
public class ClientActivator implements BundleActivator {
    // The service tacker object.
    private ServiceTracker m_tracker = null;
    private ScheduledExecutorService scheduledThreadPool;

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println(">>> [client] start bundle");

        // 打开监听管道
        m_tracker = new ServiceTracker(context,
                context.createFilter(String.format("(&(objectClass=%s)(helloService=*))", HelloService.class.getName())),
                null);
        m_tracker.open(); // 开启追踪器

        scheduledThreadPool = Executors.newScheduledThreadPool(1);
        scheduledThreadPool.scheduleAtFixedRate( // 周期线程池
                new TaskRunner(scheduledThreadPool, m_tracker), // 执行线程
                1, // 延迟1秒
                5, // 每隔3秒执行一次
                TimeUnit.SECONDS // 执行周期粒度
        );
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println(">>> [client] stop bundle");

        m_tracker.close(); // 关闭追踪器

        scheduledThreadPool.shutdown(); // 平滑的关闭ExecutorService，当此方法被调用时，ExecutorService停止接收新的任务并且等待已经提交的任务（包含提交正在执行和提交未执行）执行完成。当所有提交任务执行完毕，线程池即被关闭。
        if (!scheduledThreadPool.awaitTermination(5, TimeUnit.SECONDS)) // 当线程超时后还没有关闭，则强制关闭
            scheduledThreadPool.shutdownNow();
    }
}