package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.User;

public interface UserMapper extends BaseMapper<User> {
    // 继承MyBatis-Plus获得强大的单表增删改查能力，无需手写基础SQL
}