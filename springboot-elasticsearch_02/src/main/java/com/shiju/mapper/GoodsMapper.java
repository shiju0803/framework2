package com.shiju.mapper;

import com.shiju.domain.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shiju
 * @date 2021/06/21 10:38
 */
@Mapper
public interface GoodsMapper {
    @Select("select id,title,price,stock,saleNum,createTime,categoryName,brandName,spec specStr from goods")
    List<Goods> findAll();
}
