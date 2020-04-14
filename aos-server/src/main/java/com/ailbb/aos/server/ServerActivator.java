package com.ailbb.aos.server;

import com.ailbb.aos.api.HelloService;
import com.ailbb.aos.server.impl.HelloServiceImpl;
import org.osgi.framework.*;

import java.util.*;

/**
 * Created by Wz on 4/23/2019.
 */
public class ServerActivator implements BundleActivator {
    // The spell checker service registration.
    private ServiceRegistration m_reg = null;

    /**
     * 开始事件
     * @param context
     * @throws Exception
     */
    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println(">>> [server] start bundle");

        Dictionary dictionary = new Properties();
        dictionary.put("helloService", "serverService");

        m_reg = context.registerService(HelloService.class.getName(), new HelloServiceImpl(), dictionary);
    }

    /**
     * 停止事件
     * @param context
     * @throws Exception
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println(">>> [server] stop bundle");
        m_reg.unregister();
    }

}