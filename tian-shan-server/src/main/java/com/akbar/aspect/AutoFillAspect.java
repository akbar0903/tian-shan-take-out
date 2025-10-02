package com.akbar.aspect;

import com.akbar.annotation.AutoFill;
import com.akbar.constant.AutoFillConstant;
import com.akbar.context.BaseContext;
import com.akbar.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点：
     * 拦截 service 层 impl 包下，
     * 所有带有 @AutoFill 注解的方法。
     */
    @Pointcut("execution(* com.akbar.service..impl.*.*(..)) && @annotation(com.akbar.annotation.AutoFill)")
    public void autoFillPointcut() {
    }


    /**
     * 前置通知：
     */
    @Before("autoFillPointcut()")
    public void autoFillBefore(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充...");

        // 获取当前拦截的方法的操作类型(方法签名)
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 获取签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);// 获取方法的注解对象
        OperationType operationType = autoFill.value();// 获取操作类型

        // 获取当前被拦截的方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.warn("方法参数为空，无法进行自动填充...");
            return;
        }
        // 约定第一个参数就是实体对象
        Object entity = args[0];

        // 填充的信息
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = BaseContext.getCurrentId();

        // 根据不同的操作类型，对entity进行赋值操作
        if (operationType == OperationType.INSERT) {
            // 统一为4个字段赋值
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用方法，设置属性值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentUserId);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // 通过反射调用方法，设置属性值
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
