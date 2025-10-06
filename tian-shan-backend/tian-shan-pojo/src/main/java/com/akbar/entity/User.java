package com.akbar.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 用户信息
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("user")
public class User implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 头像
     */
    private String avatar;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
