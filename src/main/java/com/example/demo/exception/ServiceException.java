package com.example.demo.exception;

import lombok.Getter;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-01
 */
@Getter
public class ServiceException extends RuntimeException{
    private final String code;
    public ServiceException(String msg){
        super(msg);
        this.code = "500";
    }
    public ServiceException(String code,String msg){
        super(msg);
        this.code = code;
    }
}