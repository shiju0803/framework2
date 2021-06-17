package com.shiju.mapper;

import com.shiju.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shiju
 * @date 2021/06/16 16:18
 */
@Mapper
public interface UserXmlMapper {
    List<User> findAll();
}
