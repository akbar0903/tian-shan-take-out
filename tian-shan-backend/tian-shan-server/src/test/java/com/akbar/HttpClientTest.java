package com.akbar;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

//@SpringBootTest
public class HttpClientTest {

    /**
     * 通过httpClient发送GET请求
     */
    @Test
    public void testGet() throws IOException {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建httpGet请求对象
        HttpGet httpGet = new HttpGet("http://www.baidu.com");

        // 发送请求，并获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 获取状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务器返回的状态码：" + statusCode);

        // 获取响应内容
        HttpEntity entity = response.getEntity();
        String responseContent = EntityUtils.toString(entity);
        System.out.println("服务器返回的内容：" + responseContent);

        // 关闭资源
        response.close();
        httpClient.close();
    }


    /**
     * 通过httpClient发送POST请求
     */
    @Test
    public void testPost() throws IOException {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建httpPost请求对象
        HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");

        // 设置json请求体
        JSONObject json = new JSONObject();
        json.put("username", "admin");
        json.put("password", "123456");

        // 设置请求参数
        StringEntity entity = new StringEntity(json.toString());

        // 设置请求头
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        // 发送请求，并获取响应
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 获取状态码
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("服务器返回的状态码：" + statusCode);

        // 获取响应内容
        HttpEntity responseEntity = response.getEntity();

        String responseContent = EntityUtils.toString(responseEntity);
        System.out.println("服务器返回的内容：" + responseContent);

        // 关闭资源
        response.close();
        httpClient.close();
    }
}
