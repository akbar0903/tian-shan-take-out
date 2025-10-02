package com.akbar.service;

import com.akbar.dto.CategoryDTO;
import com.akbar.dto.CategoryPageQueryDTO;
import com.akbar.entity.Category;
import com.akbar.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface CategoryService extends IService<Category> {
    void save(CategoryDTO categoryDto);

    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    void deleteById(Long id);

    void startOrStop(Integer status, Long id);

    void update(CategoryDTO categoryDto);

    List<Category> list(Integer type);
}
