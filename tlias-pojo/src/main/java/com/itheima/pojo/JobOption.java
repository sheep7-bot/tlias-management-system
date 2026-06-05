package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 职位统计结果封装类
 * 用于封装职位列表和对应人数列表的统计数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobOption {

    /**
     * 职位名称列表
     * 例如：["班主任", "讲师", "学工主管", "教研主管", "咨询师"]
     */
    private List<String> jobList;

    /**
     * 对应职位的人数列表
     * 例如：[6, 13, 1, 1, 8]
     */
    private List<Integer> dataList;
}
