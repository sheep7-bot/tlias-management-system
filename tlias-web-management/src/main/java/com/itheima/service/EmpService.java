package com.itheima.service;

import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.PageResult;

import java.time.LocalDate;
import java.util.List;

/**
 * 员工业务层接口
 * 定义员工相关的业务方法
 */
public interface EmpService {

    /**
     * 员工条件分页查询
     *
     * @param name 员工姓名（支持模糊查询）
     * @param gender 员工性别（1=男，2=女）
     * @param begin 入职开始日期
     * @param end 入职结束日期
     * @param page 当前页码（从1开始）
     * @param pageSize 每页显示的记录数
     * @return 分页查询结果（包含总记录数和当前页数据列表）
     */
    PageResult pageQuery(String name, Integer gender,
                         LocalDate begin, LocalDate end,
                         Integer page, Integer pageSize);

    /**
     * 新增员工
     *
     * @param emp 员工对象（包含基本信息和工作经历列表）
     */
    void save(Emp emp);


    /**
     * 批量删除员工
     *
     * @param ids 员工ID列表
     */
    void deleteByIds(List<Integer> ids);

    /**
     * 根据ID查询员工详情
     *
     * @param id 员工ID
     * @return 员工对象（包含工作经历列表），不存在返回null
     */
    Emp getById(Integer id);

    /**
     * 更新员工信息
     *
     * @param emp 员工对象（必须包含id）
     */
    void update(Emp emp);

    /**
     * 员工登录
     *
     * @param emp 包含用户名和密码的员工对象
     * @return 登录成功返回 LoginInfo（含 JWT 令牌），失败返回 null
     */
    LoginInfo login(Emp emp);

    /**
     * 查询全部员工
     * @return 全部员工列表
     */
    List<Emp> findAll();
}
