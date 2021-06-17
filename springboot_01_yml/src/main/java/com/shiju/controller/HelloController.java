package com.shiju.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shiju
 * @date 2021/06/16 10:43
 */
@RestController
public class HelloController {
    @RequestMapping("hello")
    public String sayHello() {
        return "黄桂田，勇闯天涯！！";
    }
}
