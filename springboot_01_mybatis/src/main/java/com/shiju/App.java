package com.shiju;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shiju
 * @date 2021/06/16 16:12
 */
@SpringBootApplication
// @MapperScan("com.shiju.aaa") //扫描mapper包下注解
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
