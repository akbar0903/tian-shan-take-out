package com.akbar.service.impl;

import com.akbar.annotation.AutoFill;
import com.akbar.constant.MessageConstant;
import com.akbar.constant.StatusConstant;
import com.akbar.dto.CategoryPageQueryDTO;
import com.akbar.entity.Category;
import com.akbar.entity.Dish;
import com.akbar.entity.Setmeal;
import com.akbar.enumeration.OperationType;
import com.akbar.exception.DeletionNotAllowedException;
import com.akbar.mapper.CategoryMapper;
import com.akbar.mapper.DishMapper;
import com.akbar.mapper.SetmealMapper;
import com.akbar.result.PageResult;
import com.akbar.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;


    /**
     * 新增分类
     */
    @AutoFill(OperationType.INSERT)
    @Override
    public void insert(Category category) {
        // 默认状态为停用
        category.setStatus(StatusConstant.DISABLE);

        categoryMapper.insert(category);
    }


    /**
     * 分页查询分类
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> page = Page.of(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>()
                .like(categoryPageQueryDTO.getName() != null, Category::getName, categoryPageQueryDTO.getName())
                .eq(categoryPageQueryDTO.getType() != null, Category::getType, categoryPageQueryDTO.getType())
                .orderByDesc(Category::getSort, Category::getCreateTime);

        categoryMapper.selectPage(page, queryWrapper);
        return new PageResult(page.getTotal(), page.getRecords());
    }


    /**
     * 删除分类
     */
    @Override
    public void deleteById(Long id) {

        // 当前分类下有菜品，不能删除
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, id);
        Long dishCount = dishMapper.selectCount(dishQueryWrapper);
        if (dishCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        // 当前分类下有套餐，不能删除
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, id);
        Long setmealCount = setmealMapper.selectCount(setmealQueryWrapper);
        if (setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }


    /**
     * 启用或停用分类
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        categoryMapper.updateById(category);
    }


    /**
     * 更新分类
     */
    @AutoFill(OperationType.UPDATE)
    @Override
    public void update(Category category) {

        categoryMapper.updateById(category);
    }


    /**
     * 根据类型查询分类列表
     */
    @Override
    public List<Category> list(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>()
                .eq(type != null, Category::getType, type)
                .orderByDesc(Category::getSort, Category::getCreateTime);

        return categoryMapper.selectList(queryWrapper);
    }
}
