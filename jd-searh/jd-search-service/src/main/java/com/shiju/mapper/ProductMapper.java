package com.shiju.mapper;

import com.shiju.domain.Product;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shiju
 * @date 2021/06/18 20:01
 */
@Mapper
public interface ProductMapper {
    /**
     * 查询全部商品信息
     */
    @Select("select * from j_product")
    List<Product> findAll();


    /**
     * 条件搜索
     */
    @Select("select * from j_product where title like concat('%',#{word},'%')")
    List<Product> search(String word);

    /**
     * 添加商品信息
     */
    @Insert("insert into j_product values(null,#{title},#{price},#{commit},#{shop},#{img},#{href},#{mark})")
    void add(Product product);
}
