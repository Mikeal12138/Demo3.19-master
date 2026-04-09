package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper // 标识该接口为 MyBatis 的 Mapper，交由 Spring 容器管理代理对象
public interface UserInfoMapper extends BaseMapper<UserInfo> {
    // 继承 BaseMapper
}