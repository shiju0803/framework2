package com.shiju.controller;

import com.shiju.config.PersonProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shiju
 * @date 2021/06/16 14:33
 */
@RestController
public class PersonController {
    @Autowired
    private PersonProperties person;

    @RequestMapping("person")
    public String Test() {
        System.out.println(person);
        return person.getName() + "，吔屎啦！";
    }
}
