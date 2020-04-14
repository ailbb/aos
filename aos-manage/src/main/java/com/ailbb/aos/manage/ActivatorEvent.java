package com.ailbb.aos.manage;

import com.ailbb.aos.api.HelloService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import java.util.List;
import java.util.Map;

/**
 * Created by Wz on 4/24/2019.
 */
public class ActivatorEvent {
    // Bundle's context.
    private BundleContext m_context;
    // List of available dictionary service references.
    private List<ServiceReference> m_refList;
    // Maps service references to service objects.
    private Map<ServiceReference, HelloService> m_refToObjMap;

    public ActivatorEvent(BundleContext m_context, List<ServiceReference> m_refList, Map<ServiceReference, HelloService> m_refToObjMap) {
        this.m_context = m_context;
        this.m_refList = m_refList;
        this.m_refToObjMap = m_refToObjMap;
    }

    void onStart(ServiceReference[] refs){
        for(ServiceReference ref : refs) {
            Object service = m_context.getService(ref);

            if(null == service || null != m_refToObjMap.get(ref)) return;

            System.out.println(">>> [manage] add bundle service, "+ m_refList.size());

            m_refList.add(ref);
            m_refToObjMap.put(ref, (HelloService)service);
        }
    }


    /**
     * 注册事件
     * @param ref
     */
    void onRegistered(ServiceReference ref){
        Object service = m_context.getService(ref);

        if(null == service) return;

        if(null != m_refToObjMap.get(ref)) return;

        m_refList.add(ref);
        m_refToObjMap.put(ref, (HelloService)service);

        System.out.println(">>> [manage] changed onRegistered, "+ m_refList.size());
    }

    /**
     * 取消注册事件
     * @param ref
     */
    void onUnregistering(ServiceReference ref) {
        if(null == m_refToObjMap.get(ref)) return;

        m_context.ungetService(ref);
        m_refList.remove(ref);
        m_refToObjMap.remove(ref);

        System.out.println(">>> [manage] changed onUnregistering, "+ m_refList.size());
    }

    /**
     * 修改事件
     * @param ref
     */
    void onModified(ServiceReference ref) {
        System.out.println(">>> [manage] changed server onModified, "+ m_refList.size());
    }

}
