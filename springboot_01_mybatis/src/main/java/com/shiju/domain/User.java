package com.shiju.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author shiju
 * @date 2021/06/16 16:17
 */
@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
}
