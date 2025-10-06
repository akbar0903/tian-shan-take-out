package com.akbar.controller.user;


import com.akbar.constant.StatusConstant;
import com.akbar.entity.Dish;
import com.akbar.result.Result;
import com.akbar.service.DishService;
import com.akbar.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Tag(name = "C端-菜品浏览接口")
public class DishController {


    @Autowired
    private DishService dishService;

    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        List<DishVO> list = dishService.listWithFlavor(categoryId);

        return Result.success(list);
    }

}
