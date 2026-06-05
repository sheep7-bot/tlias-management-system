package com.itheima.aop;

import com.itheima.anno.Log;
import com.itheima.mapper.OperateLogMapper;
import com.itheima.pojo.OperateLog;
import com.itheima.utils.CurrentHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面类
 * 拦截所有加了 @Log 注解的方法，自动记录操作日志
 */
@Component
@Aspect
public class OperationLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Around("@annotation(com.itheima.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1. 记录开始时间
        long startTime = System.currentTimeMillis();

        // 2. 执行原始方法
        Object result = joinPoint.proceed();

        // 3. 记录结束时间，算耗时
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;

        // 4. 组装日志信息
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(CurrentHolder.getCurrentId());  // 操作人ID
        operateLog.setOperateTime(LocalDateTime.now());            // 操作时间
        operateLog.setClassName(joinPoint.getTarget().getClass().getName()); // 类名
        operateLog.setMethodName(joinPoint.getSignature().getName());        // 方法名
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));    // 参数
        operateLog.setReturnValue(result == null ? null : result.toString());// 返回值
        operateLog.setCostTime(costTime);                          // 耗时

        // 5. 保存到数据库
        operateLogMapper.insert(operateLog);

        return result;
    }
}
