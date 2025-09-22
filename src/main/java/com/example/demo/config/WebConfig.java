package com.example.demo.config;

//import com.example.demo.interceptor.JwtInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-19
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Resource
//    private JwtInterceptor jwtInterceptor;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/file/download/**")
                .addResourceLocations("file:./files/")  // 指向项目根目录的files文件夹
                .setCachePeriod(3600);  // 缓存1小时
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/login","/register");
//    }
}