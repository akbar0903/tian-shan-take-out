package com.akbar.service;

import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.result.PageResult;
import com.akbar.vo.DishVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface DishService extends IService<Dish> {

    void saverWithFlavors(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO queryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavors(Long id);

    void updateWithFlavors(DishDTO dishDTO);

    void startOrStop(Integer status, Long id);

    List<DishVO> listWithFlavor(Long categoryId);

    List<Dish> list(Integer categoryId);
}
