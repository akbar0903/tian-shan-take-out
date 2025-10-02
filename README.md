# 天山外卖项目

## 一、AOP公共字段自动填充

**思路：**

| 序号 | 字段名         | 含义    | 数据类型     | 操作类型(service层方法名) |
|----|-------------|-------|----------|-------------------|
| 1  | create_time | 创建时间  | datetime | insert            |
| 2  | create_user | 创建人id | bigint   | update            |
| 3  | update_time | 更新时间  | datetime | insert, update    |
| 4  | update_user | 更新人id | bigint   | insert, update    |

**实现方法：**

1. 自定义注解`AutoFill`，用于标识需要自动填充的方法
2. 自定义切面类`AutoFillAspect`，统一拦截有`AutoFill`注解的方法，通过反射给公共字段赋值
3. 在service层方法上添加`@AutoFill`注解，标识需要自动填充的公共字段

**技术点：**

- 枚举
- 注解
- AOP切面编程
- 反射

**统一做个约定：**

Controller层中调用的Service层方法update，insert的参数都是entity对象，比如EmployeeController中调用的update，service层方法的参数是Employee。