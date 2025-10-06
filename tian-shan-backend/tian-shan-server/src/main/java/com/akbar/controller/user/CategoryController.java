package com.akbar.controller.user;

import com.akbar.entity.Category;
import com.akbar.result.Result;
import com.akbar.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "C端-分类浏览接口")
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    /**
     * 根据类型查询分类列表
     */
    @Operation(summary = "根据类型查询分类列表")
    @GetMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("查询分类列表：{}", type);

        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
