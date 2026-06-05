package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页查询响应结果封装类
 * 用于统一返回分页数据格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult {

    /**
     * 总记录数（符合条件的数据总数）
     */
    private Long total;

    /**
     * 当前页的数据列表
     */
    private List<?> rows;
}
