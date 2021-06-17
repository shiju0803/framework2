package com.itheima.service.impl;

import com.itheima.service.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService //发布服务，低版dubbo本使用alibaba包下的@Service注解
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String name) {
        return "hello "+name;
    }
}
