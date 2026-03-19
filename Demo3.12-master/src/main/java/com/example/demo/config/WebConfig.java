package com.example.demo.config;

import com.example.demo.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                // 拦截所有 /api 下的请求
                .addPathPatterns("/api/**")
                // 仅放行登录接口，删除 /api/users/* 这类宽泛规则
                .excludePathPatterns("/api/users/login");
    }
}