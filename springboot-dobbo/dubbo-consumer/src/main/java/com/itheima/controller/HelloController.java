package com.itheima.controller;

import com.itheima.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@DubboReference
    private HelloService helloService;

    @GetMapping(value = "/sayHello",produces = "text/html;charset=utf-8")
    public String sayHello(@RequestParam(defaultValue = "snake") String name){
        return helloService.sayHello(name);
    }
}
