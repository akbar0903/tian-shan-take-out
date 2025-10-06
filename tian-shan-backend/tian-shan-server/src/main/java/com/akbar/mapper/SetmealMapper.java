package com.akbar.mapper;

import com.akbar.entity.Setmeal;
import com.akbar.vo.DishItemVO;
import com.akbar.vo.SetmealVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    Page<SetmealVO> pageQuery(Page<SetmealVO> page,
                              String name,
                              Integer categoryId,
                              Integer status);

    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}