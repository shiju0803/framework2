package com.shiju.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shiju
 * @date 2021/06/16 10:00
 */
@RestController
public class HelloController {

    @RequestMapping("hello")
    public String hello(String name) {
        return name + "说他想勇闯天涯！！！";
    }
}
