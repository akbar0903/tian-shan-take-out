package com.akbar.controller.admin;

import com.akbar.dto.DishDTO;
import com.akbar.dto.DishPageQueryDTO;
import com.akbar.result.PageResult;
import com.akbar.result.Result;
import com.akbar.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;


    /**
     * 添加菜品和其对应的口味
     */
    @PostMapping
    public Result<Void> save(@RequestBody DishDTO dishDTO) {
        dishService.saverWithFlavors(dishDTO);
        return Result.success();
    }


    /**
     * 菜品分页查询
     */
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
    @DeleteMapping()
    public Result<Void> delete(@RequestParam("ids") List<Long> ids) {
        log.info("要删除的id列表为： {}", ids);
        dishService.deleteBatch(ids);

        return Result.success();
    }
}
