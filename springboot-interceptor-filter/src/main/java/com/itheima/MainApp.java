package com.itheima;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
//@WebFilter这个注解是Servlet3.0的规范，并不是Spring boot提供的,需要进行扫包
@ServletComponentScan("com.itheima.filter")
public class MainApp {

    public static void main(String[] args) {
        SpringApplication.run(MainApp.class,args);
    }
}
