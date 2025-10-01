package com.akbar.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

/**
 * <p>
 * 菜品口味关系表
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("dish_flavor")
public class DishFlavor {

    /**
     * 主键
     */
    private Long id;

    /**
     * 菜品
     */
    private Long dishId;

    /**
     * 口味名称
     */
    private String name;

    /**
     * 口味数据list
     */
    private String value;
}
