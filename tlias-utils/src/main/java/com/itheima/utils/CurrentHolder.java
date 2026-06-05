package com.itheima.utils;

/**
 * 存储当前登录用户ID的工具类
 * 基于 ThreadLocal，每个请求线程独立存储
 */
public class CurrentHolder {

    private static final ThreadLocal<Integer> CURRENT_LOCAL = new ThreadLocal<>();

    /**
     * 存储当前登录员工ID
     */
    public static void setCurrentId(Integer id) {
        CURRENT_LOCAL.set(id);
    }

    /**
     * 获取当前登录员工ID
     */
    public static Integer getCurrentId() {
        return CURRENT_LOCAL.get();
    }

    /**
     * 移除（请求结束后调用，防止内存泄漏）
     */
    public static void remove() {
        CURRENT_LOCAL.remove();
    }
}
