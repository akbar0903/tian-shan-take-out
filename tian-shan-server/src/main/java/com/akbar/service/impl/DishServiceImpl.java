package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.constant.StatusConstant;
import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.entity.DishFlavor;
import com.akbar.entity.SetmealDish;
import com.akbar.exception.DeletionNotAllowedException;
import com.akbar.mapper.DishFlavorMapper;
import com.akbar.mapper.DishMapper;
import com.akbar.mapper.SetmealDishMapper;
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
import java.util.Objects;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


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


    /**
     * 删除菜品
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 如果是启售中，不能删
        List<Dish> dishes = dishMapper.selectByIds(ids);
        for (Dish dish : dishes) {
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 如果菜品跟套餐有关联，不能删
/*        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>()
                .select(SetmealDish::getDishId)
                .in(SetmealDish::getDishId, ids);
        List<SetmealDish> setmealDishIds= setmealDishMapper.selectList(queryWrapper);
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }*/

        // 删除菜品

        // 删除菜品对应的口味
    }
}
