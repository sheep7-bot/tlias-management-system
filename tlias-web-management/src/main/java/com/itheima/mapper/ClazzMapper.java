package com.itheima.mapper;

import com.itheima.pojo.Clazz;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ClazzMapper {

    @Insert("insert into clazz VALUES (null,#{name},#{room},#{beginDate},#{endDate},#{masterId},#{subject},#{createTime},#{updateTime})")
    void insert(Clazz clazz);

    List<Clazz> list(String name, LocalDate begin, LocalDate end);

    @Select("select * from clazz where id = #{id}")
    Clazz getInfo(Integer id);

    void update(Clazz clazz);

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from clazz")
    List<Clazz> findAll();
}
