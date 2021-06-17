package com.shiju.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shiju
 * @date 2021/06/16 14:25
 */
@Component
@ConfigurationProperties(prefix = "person")
@Getter
@Setter
@ToString
public class PersonProperties {
    private String name;
    private int age;
    private String desc;
}
