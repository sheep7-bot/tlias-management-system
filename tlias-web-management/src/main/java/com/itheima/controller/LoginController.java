package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.LoginInfo;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录 Controller
 */
@Slf4j
@RestController
public class LoginController {

    @Autowired
    private EmpService empService;

    /**
     * 员工登录
     *
     * @param emp 包含 username 和 password 的员工信息
     * @return 登录成功返回用户信息 + JWT 令牌，失败返回错误信息
     */
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {
        log.info("登录: {}", emp);
        LoginInfo info = empService.login(emp);
        if (info != null) {
            return Result.success(info);
        }
        return Result.error("用户名或密码错误");
    }
}
