package com.shiju.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shiju
 * @date 2021/06/18 19:32
 */
@Data
public class Product implements Serializable {
    private Integer id;
    private String title;
    private String price;
    private String commit;
    private String shop;
    private String img;
    private String href;
    private String mark;
}
