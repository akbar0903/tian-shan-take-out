package com.akbar.mapper;

import com.akbar.entity.Dish;
import com.akbar.vo.DishVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 分页查询菜品和其对应的分类
     */
    Page<DishVO> pageQuery(Page<DishVO> page,
                           String name,
                           Integer status,
                           Long categoryId);
}
