package com.akbar.service;

import com.akbar.dto.SetmealDTO;
import com.akbar.dto.SetmealPageQueryDTO;
import com.akbar.entity.Setmeal;
import com.akbar.result.PageResult;
import com.akbar.vo.DishItemVO;
import com.akbar.vo.SetmealVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    List<Setmeal> list(Long categoryId);

    List<DishItemVO> getDishItemById(Long id);

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);
}
