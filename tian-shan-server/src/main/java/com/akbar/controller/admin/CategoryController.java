package com.akbar.controller.admin;

import com.akbar.dto.CategoryDTO;
import com.akbar.dto.CategoryPageQueryDTO;
import com.akbar.entity.Category;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "分类管理")
@RestController
@RequestMapping("/admin/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     */
    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Void> save(@RequestBody CategoryDTO categoryDto) {
        categoryService.save(categoryDto);
        return Result.success();
    }


    /**
     * 分页查询分类
     */
    @Operation(summary = "分页查询分类")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 删除分类
     */
    @Operation(summary = "删除分类")
    @DeleteMapping
    public Result<String> delete(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }


    /**
     * 启用禁用分类
     */
    @Operation(summary = "启用禁用分类")
    @PostMapping("/status/{status}")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        categoryService.startOrStop(status, id);
        return Result.success();
    }


    /**
     * 更新分类
     */
    @Operation(summary = "更新分类")
    @PutMapping
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        categoryService.update(categoryDTO);
        return Result.success();
    }


    /**
     * 根据类型查询分类
     */
    @Operation(summary = "根据类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }

}
