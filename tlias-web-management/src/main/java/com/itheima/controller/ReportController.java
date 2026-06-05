package com.itheima.controller;

import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;
import com.itheima.pojo.Result;
import com.itheima.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 数据统计报表 Controller
 * 提供各类统计数据的 RESTful API 接口
 */
@Slf4j
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 员工性别统计接口
     *
     * 请求路径：GET /report/empGenderData
     * 请求方式：GET
     * 接口描述：统计员工性别信息，返回男性和女性员工的数量
     *
     * @return 统一响应结果，data 为性别统计数据列表
     *
     * 响应示例：
     * {
     *   "code": 1,
     *   "msg": "success",
     *   "data": [
     *     {"name": "男性员工", "value": 5},
     *     {"name": "女性员工", "value": 6}
     *   ]
     * }
     */
    @GetMapping("/empGenderData")
    public Result getEmpGenderData() {
        log.info("查询员工性别统计数据");

        List<Map<String, Object>> genderData = reportService.getEmpGenderStats();

        log.info("员工性别统计查询成功");
        return Result.success(genderData);
    }

    /**
     * 员工职位人数统计接口
     *
     * 请求路径：GET /report/empJobData
     * 请求方式：GET
     * 接口描述：统计各个职位的员工人数
     *
     * @return 统一响应结果，data 为职位统计结果对象
     *
     * 响应示例：
     * {
     *   "code": 1,
     *   "msg": "success",
     *   "data": {
     *     "jobList": ["教研主管", "学工主管", "其他", "班主任", "咨询师", "讲师"],
     *     "dataList": [1, 1, 2, 6, 8, 13]
     *   }
     * }
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData() {
        log.info("查询员工职位统计数据");

        JobOption jobOption = reportService.getEmpJobStats();

        log.info("员工职位统计查询成功");
        return Result.success(jobOption);
    }

    /**
     * 学员学历统计
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("统计学员的学历信息");
        List<Map> dataList = reportService.getStudentDegreeData();
        return Result.success(dataList);
    }

    /**
     * 班级人数统计
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("班级人数统计");
        ClazzCountOption clazzCountOption = reportService.getStudentCountData();
        return Result.success(clazzCountOption);
    }
}
