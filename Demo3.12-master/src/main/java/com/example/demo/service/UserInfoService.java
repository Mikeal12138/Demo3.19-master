package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.entity.UserInfo;
import com.example.demo.vo.UserDetailVO;

public interface UserInfoService {
    Result<Object> getUserInfoPage(Integer pageNum, Integer pageSize);
    Result<UserDetailVO> getUserDetail(Long userId);
    Result<String> updateUserInfo(UserInfo userInfo);
}