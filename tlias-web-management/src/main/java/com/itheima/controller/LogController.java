package com.itheima.controller;

import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 操作日志 Controller
 * 提供操作日志相关的 RESTful API 接口
 */
@Slf4j
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private OperateLogService operateLogService;

    /**
     * 操作日志分页查询接口
     *
     * 请求路径：GET /log/page
     * 请求方式：GET
     * 接口描述：分页查询操作日志列表
     *
     * @param page     当前页码（默认第1页）
     * @param pageSize 每页显示记录数（默认10条）
     * @return 统一响应结果，data 为分页数据（包含 total 和 rows）
     *
     * 请求示例：
     * GET /log/page?page=1&pageSize=10
     */
    @GetMapping("/page")
    public Result pageQuery(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info("操作日志分页查询，page: {}, pageSize: {}", page, pageSize);

        PageResult pageResult = operateLogService.pageQuery(page, pageSize);

        return Result.success(pageResult);
    }
}
