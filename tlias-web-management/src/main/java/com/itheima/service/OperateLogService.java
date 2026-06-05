package com.itheima.service;

import com.itheima.pojo.PageResult;

/**
 * 操作日志业务层接口
 * 定义操作日志相关的业务方法
 */
public interface OperateLogService {

    /**
     * 操作日志分页查询
     *
     * @param page     当前页码
     * @param pageSize 每页显示记录数
     * @return 分页查询结果
     */
    PageResult pageQuery(Integer page, Integer pageSize);
}
