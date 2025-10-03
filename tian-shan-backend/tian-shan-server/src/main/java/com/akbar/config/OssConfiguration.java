package com.akbar.config;

import com.akbar.properties.AliossProperties;
import com.akbar.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {


    @Bean
    @ConditionalOnMissingBean   // 如果容器中没有某个类型的 Bean，才会创建并注册当前方法返回的 Bean
    public AliOssUtil aliOssUtil(AliossProperties aliOssProperties) {
        log.info("初始化阿里云OSS工具类: {}", aliOssProperties);
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName(),
                aliOssProperties.getRegion()
        );
    }
}
