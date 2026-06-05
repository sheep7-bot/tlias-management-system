package com.itheima.controller;

import com.itheima.anno.Log;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.pojo.Student;
import com.itheima.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Log
    @PostMapping
    public Result save(@RequestBody Student student) {
        studentService.save(student);
        return Result.success();
    }

    @GetMapping
    public Result page(String name, Integer degree, Integer clazzId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult pageResult = studentService.page(name, degree, clazzId, page, pageSize);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        Student student = studentService.getInfo(id);
        return Result.success(student);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }

    @Log
    @DeleteMapping("/{ids}")
    public Result delete(@PathVariable List<Integer> ids) {
        studentService.delete(ids);
        return Result.success();
    }

    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result violationHandle(@PathVariable Integer id, @PathVariable Integer score) {
        studentService.violationHandle(id, score);
        return Result.success();
    }
}
