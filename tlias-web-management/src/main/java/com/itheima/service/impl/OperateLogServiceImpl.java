package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.pojo.PageResult;
import com.itheima.service.OperateLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志业务层实现类
 */
@Slf4j
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Override
    public PageResult pageQuery(Integer page, Integer pageSize) {
        log.info("操作日志分页查询，page: {}, pageSize: {}", page, pageSize);

        // 1. 设置分页参数
        PageHelper.startPage(page, pageSize);

        // 2. 执行查询（关联员工表查询操作人姓名）
        List<OperateLog> operateLogList = operateLogMapper.pageQuery();

        // 3. 提取分页信息
        Page<OperateLog> p = (Page<OperateLog>) operateLogList;

        // 4. 封装并返回
        return new PageResult(p.getTotal(), p.getResult());
    }
}
