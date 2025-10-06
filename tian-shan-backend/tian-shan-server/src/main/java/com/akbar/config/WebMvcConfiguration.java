package com.akbar.config;

import com.akbar.interceptor.JwtTokenAdminInterceptor;
import com.akbar.interceptor.JwtTokenUserInterceptor;
import com.akbar.json.JacksonObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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

    @Autowired
    private JwtTokenUserInterceptor jwtTokenUserInterceptor;

    /**
     * 自定义消息转换器的优先级
     * 不能等于0，否则swagger不能正常显示
     */
    private static final Integer CONVERT_PRIORITY = 1;


    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册管理端拦截器
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");

        // 注册用户端拦截器
        registry.addInterceptor(jwtTokenUserInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login", "/user/shop/status");
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
        converters.add(CONVERT_PRIORITY, converter);
    }


    /**
     * swagger配置
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("天山外卖系统API文档")
                        .description("天山外卖系统API文档，基于Spring Boot3，java-17，Mybatis-plus")
                        .version("1.0")
                );
    }
}
