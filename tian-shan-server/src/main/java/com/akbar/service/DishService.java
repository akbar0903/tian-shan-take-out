package com.akbar.service;

import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface DishService extends IService<Dish> {

    void saverWithFlavors(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO queryDTO);
}
