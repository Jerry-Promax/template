package com.example.demo.utils;

import cn.hutool.core.util.StrUtil;
import com.example.demo.common.BaseContext;
import com.example.demo.entity.Manager;
import com.example.demo.entity.User;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.UserMapper;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-03
 */
@Component
@Slf4j
public class TokenUtils {
    @Resource
    private UserMapper userMapper;
    private final String secret;
    private final Long expiration;
    private final UserDetailsService userDetailsService;


    @Autowired
    public TokenUtils(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") Long expiration,
            UserDetailsService userDetailsService) {
        this.secret = secret;
        this.expiration = expiration;
        this.userDetailsService = userDetailsService;
    }
    /**
     * 生成token（基于UserDetails）
     */
    public String genToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // claims是放在jwt的负载上面，我可以在上面存放任何我想放的数据，目前只放了用户名和权限
        claims.put("sub", userDetails.getUsername()); // 用户名

        // 安全获取权限
        Collection<? extends GrantedAuthority> authorities =
                Optional.ofNullable(userDetails.getAuthorities())
                        .orElse(Collections.emptyList());

        claims.put("auths", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        // 如果用户是Manager类型，添加额外信息
        if (userDetails instanceof Manager) {
            Manager manager = (Manager) userDetails;
            claims.put("id", manager.getId());
            claims.put("status", manager.getStatus());
        }

        return buildToken(claims);
    }

    private String buildToken(Map<String, Object> claims) {
        // 统一使用Base64编码后的密钥
        // String base64Secret = Base64.getEncoder().encodeToString(secret.getBytes(StandardCharsets.UTF_8));
        // 构建jwt
        return Jwts.builder()
                .setClaims(claims) // 负载
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 过期时间
                .signWith(
                        SignatureAlgorithm.HS256,
//                        base64Secret.getBytes(StandardCharsets.UTF_8)
                secret.getBytes(StandardCharsets.UTF_8) // 签名加密，密钥要指定的编码格式，才能有效签名
                )
                .compact();
    }
    // 验证Token并获取Authentication对象
    public Authentication validateToken(String token) {
        try {
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7).trim();
//            }
//
//            // 验证 Token 格式
//            String[] parts = token.split("\\.");
//            if (parts.length != 3) {
//                throw new MalformedJwtException("无效的 JWT 格式: " + token);
//            }
            // 通过密钥解析token，获取到负载上面的数据
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes(StandardCharsets.UTF_8)) // 明确指定编码
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // subject代表jwt所面向的用户
            String username = claims.getSubject();
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            return new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    userDetails.getAuthorities()
            );
        } catch (ExpiredJwtException e) {
            log.error("Token已过期: {}", e.getMessage());
            throw new ServiceException("401", "Token已过期");
        } catch (UnsupportedJwtException e) {
            log.error("不支持的JWT格式: {}", e.getMessage());
            throw new ServiceException("401", "无效的Token格式");
        } catch (MalformedJwtException e) {
            log.error("JWT格式错误: {}", e.getMessage());
            throw new ServiceException("401", "无效的Token格式");
        } catch (SignatureException e) {
            log.error("签名验证失败: {}", e.getMessage());
            throw new ServiceException("401", "无效的签名");
        } catch (IllegalArgumentException e) {
            log.error("JWT参数错误: {}", e.getMessage());
            throw new ServiceException("401", "无效的Token参数");
        } catch (Exception e) {
            log.error("详细错误信息:", e); // 添加详细日志
            throw new ServiceException("401", "Token验证失败");
        }
    }

    // 获取当前用户ID
    public Integer getUserIdFromToken(String token) {
        Claims claims = (Claims) validateToken(token);
        log.info("{}",claims.toString());
        return (Integer) claims.get("id");
    }
    // 从当前请求中获取用户
    public User getCurrentUser() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes == null) {
                log.warn("当前线程没有可用的RequestAttributes");
                return null;
            }
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            // 先从request属性中获取
            User user = (User) request.getAttribute("currentUser");
            if (user != null) {
                return user;
            }
            
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                Integer userId = getUserIdFromToken(token);
                user = userMapper.selectById(userId);
                if (user != null) {
                    request.setAttribute("currentUser", user); // 缓存到request中
                }
                return user;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
//    public static String genToken(String userId, String sign) {
//        return JWT.create().withAudience(userId) // 将 user id 保存到 token 里面，作为载荷
//                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) // 2小时后token过期
//                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥
//    }
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