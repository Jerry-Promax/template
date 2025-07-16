package com.example.demo.interceptor;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.demo.common.Ignore;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-03
 */

public class JwtInterceptor implements HandlerInterceptor {
    @Resource
    private UserMapper userMapper;
    @Resource
    private TokenUtils tokenUtils;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)){
            token = request.getParameter("token");
        }
        if (handler instanceof HandlerMethod){
            Ignore annotation = ((HandlerMethod) handler).getMethodAnnotation(Ignore.class);
            if (annotation != null){
                return true;
            }
        }
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401","请登录");
        }
        try {
            // 验证Token并获取用户ID
            Integer userId = tokenUtils.getUserIdFromToken(token);
            User user = userMapper.selectById(userId);

            if (user == null) {
                throw new ServiceException("401", "用户不存在");
            }

            // 将用户信息存入request，便于后续使用
            request.setAttribute("currentUser", user);

            return true;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("401", "无效Token");
        }
    }
}