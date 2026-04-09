package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    // 分页查询用户信息列表 - 路径为 GET /api/user-info/page
    @GetMapping("/page")
    public Result<Object> getUserInfoPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        return userInfoService.getUserInfoPage(pageNum, pageSize);
    }
}