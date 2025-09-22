package com.example.demo.common;

import lombok.Data;

import java.util.List;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-06
 */
@Data
public class Page<T> {
    private Integer total;
    private List<T> list;
}