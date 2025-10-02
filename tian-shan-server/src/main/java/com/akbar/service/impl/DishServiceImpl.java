package com.akbar.service.impl;

import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.entity.DishFlavor;
import com.akbar.mapper.DishFlavorMapper;
import com.akbar.mapper.DishMapper;
import com.akbar.result.PageResult;
import com.akbar.service.DishService;
import com.akbar.vo.DishVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 添加菜品和其对应的口味
     * 要么都添加成功，要么都添加失败
     */
    @Transactional
    @Override
    public void saverWithFlavors(DishDTO dishDTO) {
        // 添加菜品
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        // 执行完insert后，mybatis-plus自动把主键填充到entity对象中
        Long dishId = dish.getId();

        // 添加口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            // 像口味表插入n条数据
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
                dishFlavorMapper.insert(flavor);
            }
        }
    }


    /**
     * 分页查询菜品
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO queryDTO) {
        Page<DishVO> page = Page.of(queryDTO.getPage(), queryDTO.getPageSize());

        Page<DishVO> result = dishMapper.pageQuery(page,
                queryDTO.getName(),
                queryDTO.getStatus(),
                queryDTO.getCategoryId()
        );

        return new PageResult(result.getTotal(), result.getRecords());
    }
}
