package com.akbar.controller.admin;

import com.akbar.dto.CategoryDto;
import com.akbar.dto.CategoryPageQueryDTO;
import com.akbar.entity.Category;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     */
    @PostMapping
    public Result<Void> save(@RequestBody CategoryDto categoryDto) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);

        categoryService.insert(category);
        return Result.success();
    }


    /*
     * 分页查询分类
     * */
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    /*
     * 删除分类
     * */
    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }


    /*
     * 启用禁用分类
     * */
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }


    /*
     * 修改分类
     * */
    @PutMapping
    public Result<String> update(@RequestBody CategoryDto categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryService.update(category);
        return Result.success();
    }


    /*
     * 根据类型查询分类
     * */
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

}
