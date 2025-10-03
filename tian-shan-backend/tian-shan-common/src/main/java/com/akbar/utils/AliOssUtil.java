package com.akbar.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String region;

    /**
     * 上传文件到OSS
     *
     */
    public String upload(byte[] bytes, String objectName) {
        // 创建凭证提供者
        DefaultCredentialProvider credentialProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);

        // 创建OSS客户端
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, new ByteArrayInputStream(bytes));

            // 上传文件。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            log.info("文件上传成功, etag:{}", result.getETag());

            // 返回文件路径
            //文件访问路径规则 https://BucketName.Endpoint/ObjectName
            StringBuilder stringBuilder = new StringBuilder("https://");
            stringBuilder
                    .append(bucketName)
                    .append(".")
                    .append(endpoint)
                    .append("/")
                    .append(objectName);

            log.info("文件上传到:{}", stringBuilder);

            return stringBuilder.toString();

        } catch (OSSException oe) {
            log.error("OSS异常: {}, code={}, requestId={}, hostId={}",
                    oe.getErrorMessage(), oe.getErrorCode(), oe.getRequestId(), oe.getHostId(), oe
            );
        } catch (ClientException ce) {
            log.error("客户端异常: {}", ce.getMessage(), ce);
        } finally {
            ossClient.shutdown();
        }

        return null;
    }
}