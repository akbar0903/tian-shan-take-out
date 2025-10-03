package com.akbar.controller.user;

import com.akbar.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Tag(name = "店铺相关接口")
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
public class ShopController {

    private static final String SHOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 获取店铺营业状态
     */
    @Operation(summary = "获取店铺营业状态")
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("店铺营业状态为：{}", Objects.equals(status, 1) ? "营业中" : "打烊中");
        return Result.success(status);
    }
}
