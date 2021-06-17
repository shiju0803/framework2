package com.itheima.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @RequestMapping("findAll")
    public String findAll(){
        return "findAll";
    }

    @RequestMapping("save")
    public String save(){
        return "save";
    }

    @RequestMapping("update")
    public String update(){
        return "update";
    }
}
