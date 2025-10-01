package com.akbar.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.*;

/**
 * <p>
 * 套餐
 * </p>
 *
 * @author akbar
 * @since 2025-09-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("setmeal")
public class Setmeal {

    /**
     * 主键
     */
    private Long id;

    /**
     * 菜品分类id
     */
    private Long categoryId;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐价格
     */
    private BigDecimal price;

    /**
     * 售卖状态 0:停售 1:起售
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;

    /**
     * 图片
     */
    private String image;

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
