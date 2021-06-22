package com.shiju;

import com.shiju.domain.Product;

import java.io.IOException;
import java.util.List;

/**
 * @author shiju
 * @date 2021/06/18 19:59
 */
public interface ProductService {
    /**
     * 查询全部商品信息
     */
    List<Product> search(String word) throws IOException;

    /**
     * 添加商品信息
     */
    void add(Product product);
}
