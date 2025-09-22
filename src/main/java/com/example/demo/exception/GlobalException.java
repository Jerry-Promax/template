package com.example.demo.exception;

import com.example.demo.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@ControllerAdvice
public class GlobalException {
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result serviceException(ServiceException e){
        return Result.error(e.getCode(),e.getMessage());
    }
    // 新增：专门处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result handleAccessDeniedException(AccessDeniedException e) {
//        e.printStackTrace();
        return Result.error("403", "当前用户没有权限访问");
    }
    /**
     * 全局的异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result globalException(Exception e){
        e.printStackTrace();
        return Result.error("500","系统错误");
    }

}