package com.shiju.test;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shiju.domain.User;
import com.shiju.mapper.UserMapper;
import com.shiju.mapper.UserXmlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    /**
     * 测试分页插件
     */
    @Test
    public void test() {
        PageHelper.startPage(1, 4);
        List<User> list = userXmlMapper.findAll();
        PageInfo<User> info = new PageInfo<>(list);
        System.out.println(info);
    }

}
