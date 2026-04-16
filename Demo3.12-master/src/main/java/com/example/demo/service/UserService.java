package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.UserDetailVO;

public interface UserService {
    Result<String> register(UserDTO userDTO);
    Result<String> login(UserDTO userDTO);
    Result<String> getUserById(Long id);
    // 获取用户分页数据
    Result<Object> getUserPage(Integer pageNum, Integer pageSize);
    // 获取用户详情（含缓存）
    Result<UserDetailVO> getUserDetail(Long userId);
    // 更新用户信息
    Result<String> updateUserInfo(UserInfo userInfo);
    Result<String> deleteUser(Long userId);
}