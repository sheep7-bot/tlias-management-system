package com.itheima.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云OSS配置属性类
 * 从application.yml中读取OSS相关配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.oss")
public class AliyunOSSProperties {

    /**
     * OSS访问端点
     * 示例：https://oss-cn-beijing.aliyuncs.com
     */
    private String endpoint;

    /**
     * Bucket名称
     * 示例：lx-java-ai-01
     */
    private String bucketName;

    /**
     * OSS区域
     * 示例：cn-beijing
     */
    private String region;

    /**
     * AccessKey ID（可选，优先使用环境变量）
     */
    private String accessKeyId;

    /**
     * AccessKey Secret（可选，优先使用环境变量）
     */
    private String accessKeySecret;
}
