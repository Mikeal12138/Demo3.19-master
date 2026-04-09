package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.common.ResultCode;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<String> register(UserDTO userDTO) {
        // 1. 查询该用户名是否已存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        if (dbUser != null) {
            return Result.error(ResultCode.USER_HAS_EXISTED);
        }

        // 2. 组装实体对象
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());

        // 3. 插入数据库
        userMapper.insert(user);

        return Result.success("注册成功！");
    }

    @Override
    public Result<String> login(UserDTO userDTO) {
        // 1. 根据用户名查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDTO.getUsername());
        User dbUser = userMapper.selectOne(queryWrapper);

        // 2. 校验用户是否存在
        if (dbUser == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        // 3. 校验密码是否正确
        if (!dbUser.getPassword().equals(userDTO.getPassword())) {
            return Result.error(ResultCode.PASSWORD_ERROR);
        }
        // 4. 返回 Bearer token
        String token = "Bearer " + UUID.randomUUID();
        return Result.success(token);
    }

    @Override
    public Result<String> getUserById(Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error(ResultCode.USER_NOT_EXIST);
        }
        return Result.success("用户信息：id=" + user.getId() + ", username=" + user.getUsername());
    }

    @Override
    public Result<Object> getUserPage(Integer pageNum, Integer pageSize) {
        // 1. 创建分页对象（参数1：当前页码，参数2：每页显示条数）
        Page<User> pageParam = new Page<>(pageNum, pageSize);

        // 2. 执行分页查询（参数1：分页对象，参数2：查询条件 Wrapper，这里传 null 代表查询所有）
        // 框架会自动执行一条 COUNT 语句查总条数，再拼接 LIMIT 执行分页
        Page<User> resultPage = userMapper.selectPage(pageParam, null);

        // 3. 返回结果（resultPage 中包含了 records 数据列表、total 总条数、pages 总页数等信息）
        return Result.success(resultPage);
    }
}