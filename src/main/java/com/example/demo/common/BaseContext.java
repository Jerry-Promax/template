package com.example.demo.common;

/**
 * 类功能描述
 * <p>
 * 作者：jerry
 * 日期：2025-07-20
 */
public class BaseContext {
    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Integer id) {
        threadLocal.set(id);
    }

    public static Integer getCurrentId() {
        return threadLocal.get();
    }

    public static void removeCurrentId() {
        threadLocal.remove();
    }

}