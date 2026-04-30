package com.example.demo.interceptor;

import com.example.demo.common.ResultCode;
import com.example.demo.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        boolean isCreateUser = "POST".equalsIgnoreCase(method) && "/api/users".equals(uri);
        boolean isLogin = "POST".equalsIgnoreCase(method) && "/api/users/login".equals(uri);

        if (isCreateUser || isLogin) {
            return true;
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            sendErrorResponse(response, "未携带Token，请先登录");
            return false;
        }

        String token = null;
        if (authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        } else {
            token = authorizationHeader;
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null || !jwtUtil.validateToken(token, username)) {
            sendErrorResponse(response, "Token无效或已过期");
            return false;
        }

        return true;
    }

    private void sendErrorResponse(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ResultCode resultCode = ResultCode.TOKEN_INVALID;
        String errorJson = String.format(
                "{\"code\": %d, \"msg\": \"%s\", \"data\": null}",
                resultCode.getCode(),
                message
        );

        PrintWriter writer = response.getWriter();
        writer.write(errorJson);
        writer.flush();
    }
}