package com.itheima.mapper;

import com.itheima.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作日志 Mapper
 */
@Mapper
public interface OperateLogMapper {

    @Insert("INSERT INTO operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "VALUES (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime})")
    void insert(OperateLog log);

    /**
     * 分页查询操作日志（关联员工表查询操作人姓名）
     */
    @Select("SELECT ol.*, e.name AS operateEmpName " +
            "FROM operate_log ol " +
            "LEFT JOIN emp e ON ol.operate_emp_id = e.id " +
            "ORDER BY ol.operate_time DESC")
    List<OperateLog> pageQuery();
}
