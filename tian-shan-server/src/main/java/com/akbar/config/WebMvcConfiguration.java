package com.akbar.config;

import com.akbar.interceptor.JwtTokenAdminInterceptor;
import com.akbar.json.JacksonObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * web层配置
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private JwtTokenAdminInterceptor jwtTokenAdminInterceptor;


    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    /**
     * 消息转换器配置
     * 扩展springmvc消息转换器
     * 把后端返回给前端的数据统一进行转换
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 消息转换器对象
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置一个对象转换器
        converter.setObjectMapper(new JacksonObjectMapper());
        // 添加到转换器列表中, 优先级最高
        converters.add(0,converter);
    }

}
