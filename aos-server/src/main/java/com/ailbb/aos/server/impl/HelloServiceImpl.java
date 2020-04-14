package com.ailbb.aos.server.impl;

import com.ailbb.aos.api.HelloService;

/**
 * 这个maven工程（hello.impl）里面有一个实现了Hello接口的类，
 * 这个工程依赖了第一个工程hello.service
 * Created by Wz on 4/16/2019.
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public void sayHello(String name) {
        System.out.println("<<< [server hello impl] " + name);
    }
}
