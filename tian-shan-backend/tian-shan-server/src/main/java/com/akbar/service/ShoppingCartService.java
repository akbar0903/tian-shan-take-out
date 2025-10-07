package com.akbar.service;

import com.akbar.dto.ShoppingCartDTO;
import com.akbar.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    List<ShoppingCart> shoppingCartList();

    void cleanShoppingCart();

    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
