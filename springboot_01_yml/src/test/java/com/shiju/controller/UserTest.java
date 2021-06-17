package com.shiju.controller;

import com.shiju.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// @RunWith(SpringRunner.class) //使用低版本springboot时使用该注解。高版本不需要
@SpringBootTest
public class UserTest {
    @Autowired
    private UserService userService;

    @Test
    public void add() {
        userService.add();
    }
}
