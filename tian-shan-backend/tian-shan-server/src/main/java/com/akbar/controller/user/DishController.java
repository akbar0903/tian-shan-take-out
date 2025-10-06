package com.akbar.controller.user;

import com.akbar.result.Result;
import com.akbar.service.DishService;
import com.akbar.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类id查询菜品
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        // 构造key
        String key = "dish_" + categoryId;

        // 查询redis中是否存在该分类的菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (list != null && !list.isEmpty()) {
            log.info("从redis中获取到菜品数据");
            return Result.success(list);
        }

        // 如果不存在，则查询数据库，并将数据存入redis中
        list = dishService.listWithFlavor(categoryId);
        redisTemplate.opsForValue().set(key, list);
        log.info("从数据库中获取到菜品数据，并存入redis中");

        return Result.success(list);
    }

}
