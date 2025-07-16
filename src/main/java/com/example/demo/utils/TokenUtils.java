package com.example.demo.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.SignatureException;
import java.util.Date;
import java.util.Map;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-03
 */
@Component
public class TokenUtils {
    private static UserMapper staticUserMapper;
    @Resource
    private UserMapper userMapper;
    @PostConstruct
    public void setUserService(){
        staticUserMapper = userMapper;
    }
    /**
     * 生成token
     *
     * @return
     */
//    public static String genToken(String userId, String sign) {
//        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面，作为载荷
//                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
//                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
//    }
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private Long expiration;
    public String genToken(Map<String,Object> claims){
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }
    // 验证Token并获取Claims
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new ServiceException("401", "Token已过期");
        }
    }

    // 获取当前用户ID
    public Integer getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return (Integer) claims.get("id");
    }
    // 从当前请求中获取用户
    public User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                Integer userId = getUserIdFromToken(token);
                return userMapper.selectById(userId);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
//    /**
//     * 获取当前登录的用户信息
//     *
//     * @return user对象
//     */
//    public static User getCurrentUser() {
//        try {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
//            String token = request.getHeader("token");
//            if (StrUtil.isNotBlank(token)) {
//                String userId = JWT.decode(token).getAudience().get(0);
//                return staticUserMapper.selectById(Integer.valueOf(userId));
//            }
//        } catch (Exception e) {
//            return null;
//        }
//        return null;
//    }


}