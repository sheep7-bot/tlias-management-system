package com.itheima.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.mapper.EmpExprMapper;
import com.itheima.mapper.EmpMapper;
import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpExpr;
import com.itheima.pojo.EmpLog;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.PageResult;
import com.itheima.service.EmpLogService;
import com.itheima.service.EmpService;
import com.itheima.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工业务层实现类
 * 实现员工相关的具体业务逻辑
 */
@Slf4j
@Service
public class EmpServiceImpl implements EmpService {

    /**
     * 注入员工数据访问层对象
     * Spring 自动创建并注入 EmpMapper 的代理对象
     */
    @Autowired
    private EmpMapper empMapper;

    /**
     * 注入员工工作经历数据访问层对象
     * Spring 自动创建并注入 EmpExprMapper 的代理对象
     */
    @Autowired
    private EmpExprMapper empExprMapper;

    /**
     * 注入员工操作日志业务层对象
     * Spring 自动创建并注入 EmpLogService 实例
     */
    @Autowired
    private EmpLogService empLogService;

    /**
     * 员工条件分页查询实现
     *
     * 执行流程：
     * 1. 设置分页参数（PageHelper 拦截 SQL 自动添加 LIMIT）
     * 2. 执行条件查询（获取当前页数据）
     * 3. 提取总记录数和当前页数据
     * 4. 封装成分页结果返回
     *
     * @param name 员工姓名（支持模糊查询）
     * @param gender 员工性别（1=男，2=女）
     * @param begin 入职开始日期
     * @param end 入职结束日期
     * @param page 当前页码
     * @param pageSize 每页大小
     * @return 分页查询结果
     */

    @Override
    public PageResult pageQuery(String name, Integer gender,
                                LocalDate begin, LocalDate end,
                                Integer page, Integer pageSize) {
        // 记录查询日志，方便调试和监控
        log.info("员工分页查询，name: {}, gender: {}, begin: {}, end: {}, page: {}, pageSize: {}",
                name, gender, begin, end, page, pageSize);

        // ========== 步骤1：设置分页参数 ==========
        // PageHelper.startPage() 必须在查询语句之前调用
        // 它会在下一次查询时自动添加 LIMIT 子句
        // 例如：SELECT * FROM emp LIMIT 0, 10
        PageHelper.startPage(page, pageSize);

        // ========== 步骤2：执行条件查询 ==========
        // 调用 Mapper 方法进行多条件查询
        // 查询结果会被 PageHelper 拦截并自动分页
        List<Emp> empList = empMapper.findByCondition(name, gender, begin, end);

        // ========== 步骤3：提取分页信息 ==========
        // 将查询结果强制转换为 Page 对象
        // Page 对象包含了总记录数、当前页数据等完整信息
        Page<Emp> p = (Page<Emp>) empList;

        // ========== 步骤4：封装并返回结果 ==========
        // p.getTotal()：总记录数（由 PageHelper 自动查询 COUNT(*) 得到）
        // p.getResult()：当前页的数据列表
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 新增员工实现
     *
     * 执行流程：
     * 1. 校验必填项
     * 2. 设置默认密码 123456
     * 3. 设置创建时间和修改时间
     * 4. 保存员工基本信息
     * 5. 保存工作经历列表
     * 6. 记录操作日志（无论成功还是失败）
     *
     * @param emp 员工对象（包含基本信息和工作经历列表）
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        // 记录操作开始时间
        LocalDateTime startTime = LocalDateTime.now();

        // 构建操作日志信息
        StringBuilder logInfo = new StringBuilder();
        logInfo.append("新增员工操作 - ");
        logInfo.append("用户名: ").append(emp.getUsername()).append(", ");
        logInfo.append("姓名: ").append(emp.getName()).append(", ");
        logInfo.append("性别: ").append(emp.getGender() == 1 ? "男" : "女").append(", ");
        logInfo.append("部门ID: ").append(emp.getDeptId());

        try {
            // 记录请求日志，方便追踪和调试
            log.info("新增员工，员工信息：{}", emp);

            // ========== 步骤1：校验必填项 ==========
            if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) {
                throw new RuntimeException("用户名不能为空");
            }
            if (emp.getName() == null || emp.getName().trim().isEmpty()) {
                throw new RuntimeException("姓名不能为空");
            }
            if (emp.getGender() == null) {
                throw new RuntimeException("性别不能为空");
            }
            if (emp.getEntryDate() == null) {
                throw new RuntimeException("入职日期不能为空");
            }
            if (emp.getDeptId() == null) {
                throw new RuntimeException("部门不能为空");
            }

            // ========== 步骤2：设置默认密码 ==========
            emp.setPassword("123456");

            // ========== 步骤3：设置创建时间和修改时间 ==========
            LocalDateTime now = LocalDateTime.now();
            emp.setCreateTime(now);
            emp.setUpdateTime(now);

            // ========== 步骤4：保存员工基本信息 ==========
            empMapper.insert(emp);
            log.info("员工基本信息保存成功，员工ID：{}", emp.getId());

            // ========== 步骤5：保存工作经历列表 ==========
            List<EmpExpr> exprList = emp.getExprList();
            if (exprList != null && !exprList.isEmpty()) {
                // 为每个工作经历设置员工ID
                for (EmpExpr expr : exprList) {
                    expr.setEmpId(emp.getId());
                }
                // 批量插入工作经历
                empExprMapper.insertBatch(exprList);
                log.info("员工工作经历保存成功，共{}条", exprList.size());
            }

            // 操作成功，追加成功信息
            logInfo.append(" | 结果: 成功");
            logInfo.append(" | 员工ID: ").append(emp.getId());

        } finally {
            // 计算耗时
            long costTime = java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
            logInfo.append(" | 耗时: ").append(costTime).append("ms");

            // 如果执行到这里说明失败了（因为成功时已经在上面设置了成功信息）
            // 检查是否已经有成功标记
            if (!logInfo.toString().contains("结果: 成功")) {
                logInfo.append(" | 结果: 失败");
            }

            // 创建操作日志对象
            EmpLog empLog = new EmpLog();
            empLog.setOperateTime(LocalDateTime.now());
            empLog.setInfo(logInfo.toString());

            // 记录操作日志到新事务中（不受主事务回滚影响）
            try {
                empLogService.insertLog(empLog);
                log.info("操作日志记录成功");
            } catch (Exception e) {
                log.error("操作日志记录失败: {}", e.getMessage());
            }
        }
    }
    /**
     * 批量删除员工实现
     *
     * 执行流程：
     * 1. 校验ID列表是否为空
     * 2. 删除员工基本信息（会级联删除工作经历）
     * 3. 记录操作日志
     *
     * @param ids 员工ID列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        log.info("开始批量删除员工，ids: {}", ids);

        // ========== 步骤1：校验ID列表 ==========
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("删除的员工ID不能为空");
        }

        // ========== 步骤2：删除员工基本信息 ==========
        // MyBatis 会先删除外键关联的工作经历（如果配置了级联删除）
        // 或者需要先手动删除工作经历
        for (Integer id : ids) {
            // 删除工作经历（如果有）
            empExprMapper.deleteByEmpId(id);
            log.debug("已删除员工ID {} 的工作经历", id);
        }

        // 删除员工基本信息
        empMapper.deleteByIds(ids);
        log.info("员工基本信息删除成功，共删除{}条", ids.size());
    }

    /**
     * 根据ID查询员工详情实现
     *
     * 执行流程：
     * 1. 查询员工基本信息
     * 2. 查询工作经历列表
     * 3. 组装完整数据返回
     *
     * @param id 员工ID
     * @return 员工对象（包含工作经历列表）
     */
    @Override
    public Emp getById(Integer id) {
        log.info("查询员工详情，id: {}", id);

        // ========== 步骤1：查询员工基本信息 ==========
        Emp emp = empMapper.findById(id);

        if (emp != null) {
            // ========== 步骤2：查询工作经历列表 ==========
            List<EmpExpr> exprList = empExprMapper.findByEmpId(id);

            // ========== 步骤3：组装完整数据 ==========
            emp.setExprList(exprList);

            log.info("查询成功，员工姓名: {}, 工作经历数: {}", emp.getName(), exprList.size());
        } else {
            log.warn("员工不存在，id: {}", id);
        }

        return emp;
    }
    /**
     * 更新员工信息实现
     *
     * 执行流程：
     * 1. 校验必填项
     * 2. 设置修改时间
     * 3. 更新员工基本信息
     * 4. 删除旧的工作经历
     * 5. 保存新的工作经历
     *
     * @param emp 员工对象（必须包含id）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        log.info("开始更新员工信息，id: {}", emp.getId());

        // ========== 步骤1：校验必填项 ==========
        if (emp.getId() == null) {
            throw new RuntimeException("员工ID不能为空");
        }
        if (emp.getUsername() == null || emp.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (emp.getName() == null || emp.getName().trim().isEmpty()) {
            throw new RuntimeException("姓名不能为空");
        }
        if (emp.getGender() == null) {
            throw new RuntimeException("性别不能为空");
        }
        if (emp.getEntryDate() == null) {
            throw new RuntimeException("入职日期不能为空");
        }
        if (emp.getDeptId() == null) {
            throw new RuntimeException("部门不能为空");
        }

        // ========== 步骤2：设置修改时间 ==========
        emp.setUpdateTime(LocalDateTime.now());

        // ========== 步骤3：更新员工基本信息 ==========
        empMapper.update(emp);
        log.info("员工基本信息更新成功，id: {}", emp.getId());

        // ========== 步骤4：删除旧的工作经历 ==========
        empExprMapper.deleteByEmpId(emp.getId());
        log.debug("已删除员工ID {} 的旧工作经历", emp.getId());

        // ========== 步骤5：保存新的工作经历 ==========
        List<EmpExpr> exprList = emp.getExprList();
        if (exprList != null && !exprList.isEmpty()) {
            // 为每个工作经历设置员工ID
            for (EmpExpr expr : exprList) {
                expr.setEmpId(emp.getId());
            }
            // 批量插入新的工作经历
            empExprMapper.insertBatch(exprList);
            log.info("员工工作经历更新成功，共{}条", exprList.size());
        }

        log.info("员工信息更新完成，id: {}", emp.getId());
    }

    /**
     * 查询全部员工
     */
    @Override
    public List<Emp> findAll() {
        log.info("查询全部员工");
        return empMapper.findAll();
    }

    /**
     * 员工登录实现
     *
     * 执行流程：
     * 1. 调用 Mapper 根据用户名和密码查询员工
     * 2. 如果查到员工，生成 JWT 令牌并封装 LoginInfo 返回
     * 3. 如果没查到，返回 null
     *
     * @param emp 包含用户名和密码的员工对象
     * @return 登录成功返回 LoginInfo（含 JWT），失败返回 null
     */
    @Override
    public LoginInfo login(Emp emp) {
        // 1. 调用 Mapper 接口，根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);

        // 2. 判断是否存在该员工，如果存在，组装登录成功信息
        if (e != null) {
            log.info("登录成功，员工信息: {}", e);

            // 生成 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JwtUtils.generateToken(claims);

            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }

        // 3. 不存在，返回 null
        return null;
    }

}
