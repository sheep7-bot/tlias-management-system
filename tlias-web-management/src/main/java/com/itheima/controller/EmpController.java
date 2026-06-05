package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.Emp;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 员工管理 Controller
 * 提供员工相关的 RESTful API 接口
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    /**
     * 注入员工业务层对象
     * Spring 自动创建并注入 EmpService 实例
     */
    @Autowired
    private EmpService empService;

    /**
     * 员工列表分页查询接口
     *
     * 请求路径：GET /emps
     * 请求方式：GET
     * 接口描述：根据条件分页查询员工列表数据
     *
     * 支持的查询条件：
     * 1. 姓名模糊查询（可选）
     * 2. 性别精确查询（可选）
     * 3. 入职时间范围查询（可选）
     * 4. 分页参数（可选，有默认值）
     *
     * @param name 员工姓名，支持模糊匹配（例如："张" 可匹配 "张三"、"张伟"）
     * @param gender 员工性别，1=男，2=女
     * @param begin 入职开始日期，格式：yyyy-MM-dd
     * @param end 入职结束日期，格式：yyyy-MM-dd
     * @param page 当前页码，默认第1页
     * @param pageSize 每页显示记录数，默认10条
     * @return 统一响应结果，data 为分页数据（包含 total 和 rows）
     *
     * 请求示例：
     * GET /emps?name=张&gender=1&begin=2023-01-01&end=2023-12-31&page=1&pageSize=10
     */
    @GetMapping
    public Result pageQuery(
            String name,
            Integer gender,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end,
            Integer page,
            Integer pageSize) {

        // 记录请求日志，方便追踪和调试
        log.info("员工分页查询，name: {}, gender: {}, begin: {}, end: {}, page: {}, pageSize: {}",
                name, gender, begin, end, page, pageSize);

        // 调用业务层执行分页查询
        PageResult pageResult = empService.pageQuery(name, gender, begin, end, page, pageSize);

        // 返回成功响应，data 为分页结果
        return Result.success(pageResult);
    }

    /**
     * 新增员工接口
     *
     * 请求路径：POST /emps
     * 请求方式：POST
     * 接口描述：该接口用于添加员工的信息（包含基本信息和工作经历）
     *
     * @param emp 员工对象（从请求体中获取 JSON 数据）
     * @return 统一响应结果
     *
     * 请求示例：
     * POST /emps
     * Content-Type: application/json
     * Body: {
     *   "username": "linpingzhi",
     *   "name": "林平之",
     *   "gender": 1,
     *   "job": 1,
     *   "entryDate": "2022-09-18",
     *   "deptId": 1,
     *   "phone": "18809091234",
     *   "salary": 8000,
     *   "image": "https://...",
     *   "exprList": [...]
     * }
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) {
        // 记录请求日志，方便追踪和调试
        log.info("新增员工，员工信息：{}", emp);

        // 调用业务层执行新增操作（异常由全局异常处理器处理）
        empService.save(emp);

        // 返回成功响应
        log.info("员工新增成功");
        return Result.success(null);
    }

    /**
     * 根据ID查询员工详情
     *
     * 请求路径：GET /emps/{id}
     * 请求方式：GET
     * 接口描述：该接口用于根据主键ID查询员工的详细信息（包含工作经历）
     *
     * @param id 员工ID（路径参数）
     * @return 统一响应结果，data 为员工详细信息
     *
     * 请求示例：
     * GET /emps/8
     *
     * 响应示例：
     * {
     *   "code": 1,
     *   "msg": "success",
     *   "data": {
     *     "id": 2,
     *     "username": "zhangwuji",
     *     "name": "张无忌",
     *     "gender": 1,
     *     "image": "https://...",
     *     "job": 2,
     *     "salary": 8000,
     *     "entryDate": "2015-01-01",
     *     "deptId": 2,
     *     "createTime": "2022-09-01T23:06:30",
     *     "updateTime": "2022-09-02T00:29:04",
     *     "exprList": [...]
     *   }
     * }
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        log.info("根据ID查询员工详情，id: {}", id);

        // 调用业务层查询员工详情
        Emp emp = empService.getById(id);

        if (emp != null) {
            log.info("查询成功，员工姓名: {}", emp.getName());
            return Result.success(emp);
        } else {
            log.warn("员工不存在，id: {}", id);
            return Result.error("员工不存在");
        }
    }

    /**
     * 查询全部员工接口
     *
     * 请求路径：GET /emps/list
     * 请求方式：GET
     * 接口描述：该接口用于查询全部员工信息
     *
     * @return 统一响应结果，data 为全部员工列表
     */
    @GetMapping("/list")
    public Result findAll() {
        log.info("查询全部员工");
        List<Emp> empList = empService.findAll();
        return Result.success(empList);
    }

    /**
     * 修改员工接口
     *
     * 请求路径：PUT /emps
     * 请求方式：PUT
     * 接口描述：该接口用于修改员工的数据信息（包含基本信息和工作经历）
     *
     * @param emp 员工对象（从请求体中获取 JSON 数据，必须包含id）
     * @return 统一响应结果
     *
     * 请求示例：
     * PUT /emps
     * Content-Type: application/json
     * Body: {
     *   "id": 2,
     *   "username": "zhangwuji",
     *   "name": "张无忌",
     *   "gender": 1,
     *   "phone": "13712345678",
     *   "job": 2,
     *   "salary": 8000,
     *   "image": "https://...",
     *   "entryDate": "2015-01-01",
     *   "deptId": 2,
     *   "exprList": [...]
     * }
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        // 记录请求日志
        log.info("修改员工，员工信息：{}", emp);

        // 校验必填字段
        if (emp.getId() == null) {
            return Result.error("员工ID不能为空");
        }

        // 调用业务层执行更新操作（异常由全局异常处理器处理）
        empService.update(emp);

        log.info("员工修改成功，id: {}", emp.getId());
        return Result.success(null);
    }

    /**
     * 批量删除员工接口
     *
     * 请求路径：DELETE /emps?ids=1,2,3
     * 请求方式：DELETE
     * 接口描述：该接口用于批量删除员工信息（支持单个或批量删除）
     *
     * @param ids 员工ID列表（逗号分隔，如：1,2,3）
     * @return 统一响应结果
     *
     * 请求示例：
     * DELETE /emps?ids=1,2,3
     * DELETE /emps?ids=5
     */
    @Log
    @DeleteMapping
    public Result delete(@RequestParam String ids) {
        log.info("批量删除员工，ids: {}", ids);

        // 将逗号分隔的ID字符串转换为Integer列表
        List<Integer> idList = Arrays.stream(ids.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        log.info("解析后的ID列表: {}", idList);

        // 调用业务层执行删除操作（异常由全局异常处理器处理）
        empService.deleteByIds(idList);

        log.info("员工删除成功，共删除{}条记录", idList.size());
        return Result.success(null);
    }
}
