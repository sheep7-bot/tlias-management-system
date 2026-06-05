package com.itheima.mapper;

import com.itheima.pojo.Emp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 员工数据访问层接口
 * 用于操作数据库中的 emp 表
 */
@Mapper
public interface EmpMapper {

    /**
     * 根据多条件查询员工列表
     * 支持姓名模糊查询、性别精确查询、入职时间范围查询
     * 查询结果按修改时间（update_time）倒序排序
     *
     * @param name 员工姓名，支持模糊查询（可为 null 或空字符串）
     * @param gender 员工性别，1:男 2:女（可为 null）
     * @param begin 入职开始日期（可为 null）
     * @param end 入职结束日期（可为 null）
     * @return 符合条件的员工列表
     *
     * 使用示例：
     * - 只按姓名：findByCondition("张", null, null, null)
     * - 只按性别：findByCondition(null, 1, null, null)
     * - 按时间范围：findByCondition(null, null, "2023-01-01", "2023-12-31")
     * - 组合查询：findByCondition("张", 1, "2023-01-01", "2023-12-31")
     */
    List<Emp> findByCondition(
            @Param("name") String name,
            @Param("gender") Integer gender,
            @Param("begin") LocalDate begin,
            @Param("end") LocalDate end
    );
    /**
     * 新增员工
     *
     * @param emp 员工对象（包含基本信息）
     */
    void insert(Emp emp);


    /**
     * 根据ID查询员工
     *
     * @param id 员工ID
     * @return 员工对象
     */
    Emp findById(Integer id);

    /**
     * 批量删除员工
     *
     * @param ids 员工ID列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 更新员工信息
     *
     * @param emp 员工对象
     */
    void update(Emp emp);


    /**
     * 统计员工性别分布
     *
     * @return 包含性别和数量的列表
     */
    List<Map<String, Object>> countEmpGender();

    /**
     * 统计各职位的员工人数
     *
     * @return 包含职位和数量的列表
     */
    List<Map<String, Object>> countEmpJob();

    /**
     * 根据用户名和密码查询员工（登录验证）
     *
     * @param emp 包含 username 和 password 的员工对象
     * @return 匹配的员工信息，未匹配返回 null
     */
    Emp selectByUsernameAndPassword(Emp emp);

    /**
     * 查询全部员工
     * @return 全部员工列表
     */
    List<Emp> findAll();
}
