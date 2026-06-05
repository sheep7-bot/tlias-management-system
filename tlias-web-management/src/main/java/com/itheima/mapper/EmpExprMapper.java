package com.itheima.mapper;

import com.itheima.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//员工工作经历
@Mapper
public interface EmpExprMapper {
    /**
     * 批量插入员工工作经历
     *
     * @param empExprList 工作经历列表
     */
    void insertBatch(List<EmpExpr> empExprList);


    /**
     * 根据员工ID删除工作经历
     *
     * @param empId 员工ID
     */
    void deleteByEmpId(Integer empId);

    /**
     * 根据员工ID查询工作经历
     *
     * @param empId 员工ID
     * @return 工作经历列表
     */
    List<EmpExpr> findByEmpId(Integer empId);

}
