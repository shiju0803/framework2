package com.shiju.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author shiju
 * @date 2021/06/19 21:54
 */
public class Person {
    @JSONField(serialize = false) //id属性不要序列化成json
    private String id;
    private String name;
    private int age;
    private String address;

    public Person() {
    }

    public Person(String id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
