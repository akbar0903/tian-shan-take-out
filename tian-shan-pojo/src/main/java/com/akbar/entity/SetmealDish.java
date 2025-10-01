package com.akbar.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

/**
 * <p>
 * 套餐菜品关系
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("setmeal_dish")
public class SetmealDish {

    /**
     * 主键
     */
    private Long id;

    /**
     * 套餐id
     */
    private Long setmealId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 菜品名称 （冗余字段）
     */
    private String name;

    /**
     * 菜品单价（冗余字段）
     */
    private BigDecimal price;

    /**
     * 菜品份数
     */
    private Integer copies;
}
