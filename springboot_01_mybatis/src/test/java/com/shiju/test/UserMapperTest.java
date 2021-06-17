package com.shiju.test;

import com.shiju.mapper.UserMapper;
import com.shiju.mapper.UserXmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author shiju
 * @date 2021/06/16 16:19
 */
@SpringBootTest
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserXmlMapper userXmlMapper;

    @Test
    public void findAll() {
        userMapper.findAll().forEach(s -> System.out.println(s));
    }

    @Test
    public void test() {
        userXmlMapper.findAll().forEach(s -> System.out.println(s));
    }

}
