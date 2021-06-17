package com.shiju.mapper;

import com.shiju.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shiju
 * @date 2021/06/16 16:18
 */
@Mapper //表示该接口是一个mapper接口，将来由mybatis创建代理对象保存到spring容器中
@Repository //消除idea报错
public interface UserMapper {

    @Select("select * from user")
    List<User> findAll();
}
