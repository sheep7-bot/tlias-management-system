package com.itheima.mapper;

import com.itheima.pojo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeptMapper {
//    查询全部部门
    @Select("select * from dept order by update_time desc")
    public List<Dept> findAll();

    //根据ID删除部门
    @Delete("delete from dept where id = #{id}")
    public void deleteById(Integer id);

    //新增部门
    @Insert("insert into dept(name,create_time,update_time) values(#{name},#{createTime},#{updateTime})")
    void insert(Dept dept);
//    根据ID查询部门
    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept findById(Integer id);
    // 修改部门（根据 id 更新 name 和 update_time）
    @Update("UPDATE dept SET name = #{name}, update_time = #{updateTime} WHERE id = #{id}")
    void update(Dept dept);
}
