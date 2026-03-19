package com.example.demo.controller;

import com.example.demo.common.Result;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users") // 关键：添加 /api 前缀，让 URI 匹配拦截规则
public class UserController {

    // 1. 查询用户（GET /api/users/1 → 匹配放行规则B）
    @GetMapping("/{id}")
    public Result<String> getUser(@PathVariable("id") Long id) {
        return Result.success("查询成功，ID：" + id);
    }

    // 2. 注册用户（POST /api/users → 匹配放行规则A）
    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        user.setId(3L);
        return Result.success(user);
    }

    // 3. 删除用户（DELETE /api/users/1 → 敏感操作，需 Token）
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable("id") Long id) {
        return Result.success(null);
    }

    // 4. 登录接口（放行规则，无需 Token）
    @PostMapping("/login")
    public Result<String> login(@RequestParam String username, @RequestParam String password) {
        return Result.success("登录成功，Token：abc123");
    }

    // User 实体类（不变）
    public static class User {
        private Long id;
        private String name;
        private Integer age;

        // 无参/全参构造 + Getter/Setter（略）
        public User() {}
        public User(Long id, String name, Integer age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }
}