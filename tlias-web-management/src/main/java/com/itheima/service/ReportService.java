package com.itheima.service;

import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;

import java.util.List;
import java.util.Map;

/**
 * 数据统计报表服务接口
 * 提供各类统计数据的业务方法
 */
public interface ReportService {

    /**
     * 统计员工性别分布
     *
     * @return 包含男性员工数和女性员工数的列表，格式为 [{"name": "男性员工", "value": 5}, ...]
     */
    List<Map<String, Object>> getEmpGenderStats();

    /**
     * 统计各职位的员工人数
     *
     * @return 职位统计结果对象，包含职位列表和人数列表
     */
    JobOption getEmpJobStats();

    /**
     * 统计学员学历分布
     */
    List<Map> getStudentDegreeData();

    /**
     * 统计班级人数
     */
    ClazzCountOption getStudentCountData();
}
