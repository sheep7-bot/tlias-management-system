package com.itheima.service;

import com.itheima.pojo.Dept;

import java.util.List;

public interface DeptService {

    //查询所有部门
    List<Dept> findAll();

    //根据id删除部门
    void deleteById(Integer id);
    //增加部门
    void add(Dept dept);
    //根据id查询部门
    Dept findById(Integer id);
//修改部门
    void update(Dept dept);
}
