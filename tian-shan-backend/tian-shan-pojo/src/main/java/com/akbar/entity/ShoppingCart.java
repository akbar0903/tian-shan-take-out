package com.akbar.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 图片
     */
    private String image;

    /**
     * 主键
     */
    private Long userId;

    /**
     * 菜品id
     */
    private Long dishId;

    /**
     * 套餐id
     */
    private Long setmealId;

    /**
     * 口味
     */
    private String dishFlavor;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
