package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 标识该接口为 MyBatis 的 Mapper，交由 Spring 容器管理代理对象
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper
}