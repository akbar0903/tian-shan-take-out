package com.akbar.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 菜品及套餐分类
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("category")
public class Category {

    /**
     * 主键
     */
    private Long id;

    /**
     * 类型   1 菜品分类 2 套餐分类
     */
    private Integer type;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;

    /**
     * 分类状态 0:禁用，1:启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 修改人
     */
    private Long updateUser;
}
