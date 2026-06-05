package com.itheima.service;

import com.itheima.pojo.ClazzCountOption;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {
    void save(Student student);
    PageResult page(String name, Integer degree, Integer clazzId, Integer page, Integer pageSize);
    Student getInfo(Integer id);
    void update(Student student);
    void delete(List<Integer> ids);
    void violationHandle(Integer id, Integer score);
    List<Map> getStudentDegreeData();
    ClazzCountOption getStudentCountData();
}
