package com.example.demo.config;

import com.example.demo.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-03
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor()) // 添加jwt的拦截器规则
                .addPathPatterns("/**");
        super.addInterceptors(registry);
    }
    @Bean
    public JwtInterceptor jwtInterceptor(){
        return new JwtInterceptor();
    }
}