package com.itheima.interceptor;

import com.itheima.utils.CurrentHolder;
import com.itheima.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 令牌校验拦截器
 * 拦截所有请求，验证请求头中的 token
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 token
        String token = request.getHeader("token");

        // 2. 判断 token 是否存在，如果为空，响应 401 状态码
        if (token == null || token.isEmpty()) {
            log.info("令牌为空，响应 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3. 校验 token，如果无效，响应 401 状态码
        try {
            Claims claims = JwtUtils.parseToken(token);
            // 解析成功，将当前登录员工ID存入ThreadLocal
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId);
        } catch (Exception e) {
            log.info("令牌非法，响应 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 4. 校验通过，放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后，清除当前登录用户ID（防止内存泄漏）
        CurrentHolder.remove();
    }
}
