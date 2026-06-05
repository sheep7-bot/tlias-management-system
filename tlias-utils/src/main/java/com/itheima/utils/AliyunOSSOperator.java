package com.itheima.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProvider;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.common.comm.SignVersion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 阿里云OSS操作工具类
 * 提供文件上传到阿里云OSS的功能
 * 配置参数从application.yml中读取
 */
@Slf4j
@Component
public class AliyunOSSOperator {

    @Autowired
    private AliyunOSSProperties ossProperties;

    /**
     * 上传文件到阿里云OSS
     *
     * @param content 文件内容的字节数组
     * @param originalFilename 原始文件名
     * @return 文件的完整访问URL
     * @throws Exception 上传异常
     */
    public String upload(byte[] content, String originalFilename) throws Exception {
        log.info("开始上传文件到阿里云OSS，原始文件名: {}, 文件大小: {} bytes", originalFilename, content.length);

        // 创建凭证提供者（使用统一的 CredentialsProvider 接口）
        CredentialsProvider credentialsProvider;

        // 优先尝试从环境变量中获取访问凭证（推荐方式，更安全）
        try {
            credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
            log.debug("使用环境变量中的OSS凭证");
        } catch (Exception e) {
            log.warn("环境变量中未找到OSS凭证，尝试使用配置文件中的凭证");

            // 检查配置文件中是否配置了AccessKey
            if (ossProperties.getAccessKeyId() == null || ossProperties.getAccessKeySecret() == null) {
                throw new RuntimeException("未配置OSS访问凭证，请设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET，或在application.yml中配置aliyun.oss.access-key-id和access-key-secret");
            }

            // 使用配置文件中的AccessKey创建凭证提供者
            credentialsProvider = new DefaultCredentialProvider(
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret()
            );
            log.debug("使用配置文件中的OSS凭证");
        }

        // 生成文件在OSS中的存储路径和文件名
        // 格式示例: 2022-09-02-00-27-0400a3f2.jpg (年月日-时分-毫秒+UUID前4位.扩展名)
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String dateTimeStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ssSSS"));
        String newFileName = dateTimeStr + UUID.randomUUID().toString().substring(0, 4) + extension;

        log.debug("生成的OSS文件名: {}", newFileName);

        // 创建OSSClient实例
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(ossProperties.getEndpoint())
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(ossProperties.getRegion())
                .build();

        try {
            // 上传文件到OSS
            ossClient.putObject(ossProperties.getBucketName(), newFileName, new ByteArrayInputStream(content));
            log.info("文件上传成功: {}", newFileName);
        } finally {
            // 关闭OSS客户端
            ossClient.shutdown();
            log.debug("OSS客户端已关闭");
        }

        // 拼接并返回文件的完整访问URL
        // 格式: https://bucketName.endpoint/fileName
        String url = ossProperties.getEndpoint().split("//")[0] + "//"
                + ossProperties.getBucketName() + "."
                + ossProperties.getEndpoint().split("//")[1] + "/" + newFileName;

        log.info("文件访问URL: {}", url);
        return url;
    }

}
