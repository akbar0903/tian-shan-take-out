package com.akbar.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * 阿里云oss配置类
 */
@Component
@ConfigurationProperties(prefix = "tian-shan.alioss")
@Data
public class AliossProperties {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String region;

}
