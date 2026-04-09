package com.example.demo.interceptor;

import com.example.demo.common.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取本次请求的 HTTP 动词和具体路径
        String method = request.getMethod(); // GET/POST/DELETE/PUT 等
        String uri = request.getRequestURI(); // 如 /api/users 或 /api/users/1

        // 2. 精细化放行规则（仅放行合法操作）
        // 规则A：POST 请求 + 路径等于 /api/users（注册用户）
        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        // 规则B：GET 请求 + 路径以 /api/users/ 开头（查询用户）
        boolean isGetUser = "GET".equalsIgnoreCase(method) && uri.startsWith("/api/users/");
        // 规则C：POST 请求 + 路径等于 /api/users/login（登录用户）
        boolean isLogin = "POST".equalsIgnoreCase(method) && "/api/users/login".equals(uri);
        // 规则D：GET 请求 + 路径等于 /api/users/page（分页查询用户）
        boolean isUserPage = "GET".equalsIgnoreCase(method) && "/api/users/page".equals(uri);
        // 规则E：GET 请求 + 路径等于 /api/user-info/page（分页查询用户信息）
        boolean isUserInfoPage = "GET".equalsIgnoreCase(method) && "/api/user-info/page".equals(uri);

        // 满足任一合法规则 → 直接放行，无需验 Token
        if (isCreateUser || isGetUser || isLogin || isUserPage || isUserInfoPage) {
            return true;
        }

        // 3. 敏感操作（DELETE/PUT 等）→ 严格校验 Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            // 返回统一 401 错误 JSON
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ResultCode resultCode = ResultCode.TOKEN_INVALID;
            String errorJson = String.format(
                    "{\"code\": %d, \"msg\": \"%s\", \"data\": null}",
                    resultCode.getCode(),
                    "非法操作：敏感动作[" + method + "]需携带Token"
            );

            PrintWriter writer = response.getWriter();
            writer.write(errorJson);
            writer.flush();
            return false; // 拦截请求
        }

        // Token 存在 → 放行敏感操作
        return true;
    }
}