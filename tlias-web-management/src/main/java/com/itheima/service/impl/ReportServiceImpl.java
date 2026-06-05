package com.itheima.service.impl;

import com.itheima.mapper.EmpMapper;
import com.itheima.mapper.StudentMapper;
import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.JobOption;
import com.itheima.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据统计报表服务实现类
 * 实现各类统计数据的具体业务逻辑
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 统计员工性别分布实现
     *
     * 执行流程：
     * 1. 调用 Mapper 查询性别统计数据
     * 2. 将数据转换为前端需要的格式
     * 3. 返回格式化后的统计数据
     *
     * @return 包含男性员工数和女性员工数的列表
     */
    @Override
    public List<Map<String, Object>> getEmpGenderStats() {
        log.info("开始统计员工性别分布");

        List<Map<String, Object>> rawData = empMapper.countEmpGender();
        log.info("查询到性别统计数据: {}", rawData);

        log.info("员工性别统计完成");
        return rawData;
    }

    @Override
    public JobOption getEmpJobStats() {
        log.info("开始统计各职位员工人数");

        List<Map<String, Object>> rawData = empMapper.countEmpJob();
        log.info("查询到职位统计数据: {}", rawData);

        List<String> jobList = new ArrayList<>();
        List<Integer> dataList = new ArrayList<>();

        for (Map<String, Object> row : rawData) {
            jobList.add((String) row.get("jobName"));
            dataList.add(((Number) row.get("count")).intValue());
        }

        log.info("职位统计完成，共{}个职位", jobList.size());

        JobOption jobOption = new JobOption();
        jobOption.setJobList(jobList);
        jobOption.setDataList(dataList);

        return jobOption;
    }

    @Override
    public List<Map> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }

    @Override
    public ClazzCountOption getStudentCountData() {
        List<Map<String, Object>> countList = studentMapper.getStudentCount();
        if (!CollectionUtils.isEmpty(countList)) {
            List<Object> clazzList = countList.stream().map(map -> map.get("cname")).toList();
            List<Object> dataList = countList.stream().map(map -> map.get("scount")).toList();
            return new ClazzCountOption(clazzList, dataList);
        }
        return null;
    }
}
