package com.ailbb.aos.manage;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

import java.util.concurrent.ExecutorService;

/**
 * Created by Wz on 4/24/2019.
 */
public class TaskRunner implements Runnable {
    private BundleContext context;

    /**
     * 线程池
     */
    private ExecutorService executorService;

    public TaskRunner(ExecutorService executorService, BundleContext context) {
        this.executorService = executorService;
        this.context = context;
    }

    @Override
    public void run() {
        System.out.println(">>> >>> >>>");
        System.out.println(">>> [manage] timer bundles.");

        for(Bundle bundle : context.getBundles()) {
            // 如果已经启动, 则停止
            try {
                if(!bundle.getSymbolicName().startsWith("aos")) continue;

                if(bundle.getSymbolicName().contains("manage")) continue;

                if(Math.random() * 2 > 1) continue; // 随机跳过一些服务, 起到随机效果

                if(bundle.getState() == Bundle.ACTIVE) {
                    bundle.stop();
                } else {
                    bundle.start();
                }
            } catch (BundleException e) {
                e.printStackTrace();
            }
        }
    }

}
