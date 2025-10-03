package com.akbar.interceptor;

import com.akbar.constant.JwtClaimsConstant;
import com.akbar.context.BaseContext;
import com.akbar.properties.JwtProperties;
import com.akbar.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT Token拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是Controller层还是其它资源
        if (!(handler instanceof HandlerMethod)) {
            // 如果不是Controller层，直接放行
            return true;
        }

        String token = request.getHeader(jwtProperties.getAdminTokenName());
        try {
            /* 注意，不能这样写：(Long) claims.get(JwtClaimsConstant.EMP_ID);
             * 因为，如果存进去的不是Long类型，会抛出ClassCastException
             *  claims.get(...) 返回 Object。
                调用 .toString() → 把它变成字符串（比如 "123"）。
                Long.valueOf("123") 会解析成一个 Long 对象。
             */
            Claims claims = JwtUtil.parseJwt(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前登录的员工ID为：{}", empId);
            BaseContext.setCurrentId(empId);

            // 放行
            return true;
        } catch (Exception e) {
            log.error("JWT Token解析失败：{}", e.getMessage());
            response.setStatus(401);
            return false;
        }
    }


    /**
     * 在请求处理之后进行调用，清除当前线程的上下文信息
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContext.removeCurrentId();
    }
}
