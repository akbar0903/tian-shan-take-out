package com.akbar.controller.user;

import com.akbar.dto.ShoppingCartDTO;
import com.akbar.entity.ShoppingCart;
import com.akbar.result.Result;
import com.akbar.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Tag(name = "购物车模块")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    @Operation(summary = "添加商品到购物车")
    public Result<Void> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加商品到购物车：{}", shoppingCartDTO);

        shoppingCartService.addShoppingCart(shoppingCartDTO);
        return Result.success();
    }


    /**
     * 查看当前用户的购物车
     */
    @GetMapping("/list")
    @Operation(summary = "查看购物车")
    public Result<List<ShoppingCart>> list() {
        List<ShoppingCart> list = shoppingCartService.shoppingCartList();
        return Result.success(list);
    }


    /**
     * 清空当前登录用户的购物车
     */
    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public Result<Void> clear() {
        shoppingCartService.cleanShoppingCart();
        return Result.success();
    }


    /**
     * 删除购物车中一个商品
     */
    @PostMapping("/sub")
    @Operation(summary = "删除购物车中一个商品")
    public Result<Void> sub(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        shoppingCartService.subShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
