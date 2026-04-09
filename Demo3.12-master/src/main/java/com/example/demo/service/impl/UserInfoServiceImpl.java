package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.UserInfo;
import com.example.demo.mapper.UserInfoMapper;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Result<Object> getUserInfoPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象（参数1：当前页码，参数2：每页显示条数）
        Page<UserInfo> pageParam = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询（参数1：分页对象，参数2：查询条件 Wrapper，这里传 null 代表查询所有）
        // 框架会自动执行一条 COUNT 语句查总条数，再拼接 LIMIT 执行分页
        Page<UserInfo> resultPage = userInfoMapper.selectPage(pageParam, null);

        // 3. 返回结果（resultPage 中包含了 records 数据列表、total 总条数、pages 总页数等信息）
        return Result.success(resultPage);
    }
}