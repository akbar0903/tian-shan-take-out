package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.constant.StatusConstant;
import com.akbar.dto.SetmealDTO;
import com.akbar.dto.SetmealPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.entity.Setmeal;
import com.akbar.entity.SetmealDish;
import com.akbar.exception.DeletionNotAllowedException;
import com.akbar.mapper.DishMapper;
import com.akbar.mapper.SetmealDishMapper;
import com.akbar.mapper.SetmealMapper;
import com.akbar.result.PageResult;
import com.akbar.service.SetmealService;
import com.akbar.vo.DishItemVO;
import com.akbar.vo.SetmealVO;
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
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;


    /**
     * 保存套餐及菜品信息
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDTO setmealDTO) {

        // 保存套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);

        // 保存套餐对应的菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmeal.getId());
                setmealDishMapper.insert(setmealDish);
            }
        }
    }


    /**
     * 分页查询套餐信息
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO queryDTO) {
        Page<SetmealVO> page = Page.of(queryDTO.getPage(), queryDTO.getPageSize());

        Page<SetmealVO> result = setmealMapper.pageQuery(
                page,
                queryDTO.getName(),
                queryDTO.getCategoryId(),
                queryDTO.getStatus()
        );

        return new PageResult(result.getTotal(), result.getRecords());
    }


    /**
     * 批量删除套餐信息
     */
    @Override
    public void deleteBatch(List<Long> ids) {

        // 起售中的套餐不能删
        List<Setmeal> setmeals = setmealMapper.selectByIds(ids);
        for (Setmeal setmeal : setmeals) {
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }


        // 删除套餐及菜品信息
        setmealMapper.deleteByIds(ids);

        // 删除菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>().in(SetmealDish::getSetmealId, ids);
        setmealDishMapper.delete(queryWrapper);
    }


    /**
     * 根据id查询套餐及菜品信息
     */
    @Override
    public SetmealVO getByIdWithDish(Long id) {
        SetmealVO setmealVO = new SetmealVO();

        //查询套餐基本信息
        Setmeal setmeal = setmealMapper.selectById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

        //根据套餐信息查询菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishMapper.selectList(queryWrapper);
        setmealVO.setSetmealDishes(setmealDishList);

        return setmealVO;
    }


    /**
     * 更新套餐信息
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //修改套餐表，执行update
        setmealMapper.updateById(setmeal);

        //套餐id
        Long setmealId = setmealDTO.getId();

        //先删除套餐对应的所有菜品信息
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<SetmealDish>()
                .eq(SetmealDish::getSetmealId, setmealId);
        setmealDishMapper.delete(queryWrapper);

        // 再把新的菜品信息插入到关联表中
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDish.setSetmealId(setmealId);
                setmealDishMapper.insert(setmealDish);
            }
        }
    }


    /**
     * 启用或停用套餐
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.updateById(setmeal);
    }


    /**\
     * 根据分类id查询套餐列表
     */
    @Override
    public List<Setmeal> list(Long categoryId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, StatusConstant.ENABLE);
        return setmealMapper.selectList(queryWrapper);
    }


    /**
     * 根据套餐id查询包含的菜品列表
     */
    @Override
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
