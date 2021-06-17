package com.shiju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shiju
 * @date 2021/06/16 10:01
 * <p>
 * 引导类。 SpringBoot项目的入口
 * @SpringBootApplication内部默认扫描引导类所在包及其子包类中的注解
 */
@SpringBootApplication //表示是一个springboot构建的项目
public class App {
    public static void main(String[] args) {
        //App.class:将自己类的class对象传递进来，主要是解析该类上的@SpringBootApplication
        SpringApplication.run(App.class, args);
    }
}
