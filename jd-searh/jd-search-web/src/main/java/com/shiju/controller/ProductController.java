package com.shiju.controller;

import com.shiju.ProductService;
import com.shiju.domain.Product;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shiju
 * @date 2021/06/18 20:06
 */
@RestController
@RequestMapping("product")
public class ProductController {
    @DubboReference
    private ProductService productService;

    /**
     * 查询全部商品信息
     */
    @GetMapping("search/{word}")
    List<Product> search(@PathVariable String word) {
        return productService.search(word);
    }

    /**
     * 添加商品信息
     */
    @PostMapping(value = "add", produces = "text/html;charset=utf-8")
    String add(@RequestBody Product product) {
        try {
            productService.add(product);
            return "添加成功 😋😋😋😋";
        } catch (Exception e) {
            e.printStackTrace();
            return "添加失败 😱😱😱😱";
        }
    }
}
