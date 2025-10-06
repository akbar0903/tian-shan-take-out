package com.akbar.controller.admin;

import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.entity.Dish;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.DishService;
import com.akbar.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "菜品管理")
@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * 添加菜品和其对应的口味
     */
    @Operation(summary = "添加菜品和其对应的口味")
    @PostMapping
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        dishService.saverWithFlavors(dishDTO);
        return Result.success();
    }


    /**
     * 菜品分页查询
     */
    @Operation(summary = "分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO queryDTO) {
        PageResult pageResult = dishService.pageQuery(queryDTO);
        return Result.success(pageResult);
    }


    /**
     * 删除菜品
     * 前端传的是String字符串，比如1，2，3
     * 通过@RequestParam("ids") List<Long> ids这种写法，Spring会自动将String字符串转换成List<Long>
     */
    @Operation(summary = "删除菜品")
    @DeleteMapping
    public Result<Void> delete(@RequestParam("ids") List<Long> ids) {
        log.info("要删除的id列表为： {}", ids);
        dishService.deleteBatch(ids);

        return Result.success();
    }


    /**
     * 回显菜品信息
     */
    @Operation(summary = "回显菜品信息")
    @GetMapping("/{id}")
    public Result<DishVO> get(@PathVariable("id") Long id) {
        DishVO dishVO = dishService.getByIdWithFlavors(id);

        return Result.success(dishVO);
    }


    /**
     * 更新菜品及口味信息
     */
    @Operation(summary = "更新菜品及口味信息")
    @PutMapping
    public Result<Void> update(@RequestBody DishDTO dishDTO) {
        dishService.updateWithFlavors(dishDTO);

        return Result.success();
    }


    /**
     * 起售停售菜品
     */
    @Operation(summary = "起售停售菜品")
    @PostMapping("/status/{status}")
    public Result<Void> startOrStop(@PathVariable("status") Integer status, Long id) {
        dishService.startOrStop(status, id);

        return Result.success();
    }


    /**
     * 根据分类id查询菜品列表
     */
    @Operation(summary = "根据分类id查询菜品列表")
    @GetMapping("/list")
    public Result<List<Dish>> list(Integer categoryId) {
        List<Dish> dishList = dishService.list(categoryId);

        return Result.success(dishList);
    }
}
