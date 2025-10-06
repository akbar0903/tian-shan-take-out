package com.akbar.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tian-shan.wechat")
@Data
public class WeChatProperties {

    // 微信登录相关配置
    private String appid;
    private String secret;
    private String grantType;

    // 微信支付相关配置
    private String mchId;                   // 商户号
    private String mchSerialNo;             // 商户证书序列号
    private String privateKeyFilePath;      // 商户私钥文件路径
    private String apiV3Key;                // 微信支付APIv3密钥
    private String weChatPayCertFilePath;   // 平台证书文件路径
    private String notifyUrl;               // 微信支付成功通知地址
    private String refundNotifyUrl;         // 微信退款成功通知地址

}
