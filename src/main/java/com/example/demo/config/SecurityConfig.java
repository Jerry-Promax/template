package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


/**
 * `@EnableWebSecurity` 注解 deug参数为true时，开启调试模式，会有更多的debug输出
 * 启用`@EnableGlobalMethodSecurity(prePostEnabled = true)`注解后即可使用方法级安全注解
 * 方法级安全注解：
 * pre : @PreAuthorize（执行方法之前授权）  @PreFilter（执行方法之前过滤）
 * post : @PostAuthorize （执行方法之后授权） @ PostFilter（执行方法之后过滤）
 *
 * @author 硝酸铜
 * @date 2021/9/22
 */
@Configuration
@EnableWebSecurity  // 启用Spring Security
@EnableGlobalMethodSecurity(prePostEnabled = true)  // 启用方法级权限控制
@Slf4j
@RequiredArgsConstructor  // Lombok自动注入构造函数（替代@Resource）
public class SecurityConfig         {
    @Resource
    private AccessDeniedHandler customAccessDeniedHandler;
    // 跨域配置
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(Arrays.asList("http://localhost:8081"));
        cors.setAllowedMethods(Arrays.asList("*"));
        cors.setAllowedHeaders(Arrays.asList("*"));
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",cors);
        return source;
    }
    // 核心安全策略配置
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().configurationSource(corsConfigurationSource()).and()
                .csrf().disable()
                .formLogin().disable()
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/register","/file/download/**","/file/upload").permitAll()
                    // 确保其他接口有正确的权限配置
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 无状态session
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class) // 添加JWT过滤器
                .exceptionHandling(exceptions -> exceptions.accessDeniedHandler(customAccessDeniedHandler));
        return httpSecurity.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}