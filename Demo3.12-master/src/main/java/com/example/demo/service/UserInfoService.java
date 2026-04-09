package com.example.demo.service;

import com.example.demo.common.Result;

public interface UserInfoService {
    Result<Object> getUserInfoPage(Integer pageNum, Integer pageSize);
}