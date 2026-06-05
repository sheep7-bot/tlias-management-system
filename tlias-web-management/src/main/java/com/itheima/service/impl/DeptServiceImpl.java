package com.itheima.service.impl;

import com.itheima.mapper.DeptMapper;
import com.itheima.pojo.Dept;
import com.itheima.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {
    @Autowired

    private DeptMapper deptMapper;
    //查询所有部门
    @Override
    public List<Dept> findAll(){
        return deptMapper.findAll();
    }
//删除部门
    @Override
    public void deleteById(Integer id) {
        deptMapper.deleteById(id);
    }
//添加部门
    @Override
    public void add(Dept dept) {
        //1.补全基本属性-createTime,updateTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());
        // 2.调用Mapper接口方法插入数据
        deptMapper.insert(dept);
    }
  //根据id查询部门
        @Override
        public Dept findById(Integer id) {
            return deptMapper.findById(id);
        }
//        修改部门
@Override
public void update(Dept dept) {
    // 设置更新时间
    dept.setUpdateTime(LocalDateTime.now());
    // 调用 Mapper 执行更新
    deptMapper.update(dept);
}
    }

