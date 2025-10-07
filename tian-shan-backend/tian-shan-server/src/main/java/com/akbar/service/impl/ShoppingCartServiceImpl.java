package com.akbar.service.impl;

import com.akbar.context.BaseContext;
import com.akbar.dto.ShoppingCartDTO;
import com.akbar.entity.Dish;
import com.akbar.entity.Setmeal;
import com.akbar.entity.ShoppingCart;
import com.akbar.mapper.DishMapper;
import com.akbar.mapper.SetmealMapper;
import com.akbar.mapper.ShoppingCartMapper;
import com.akbar.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;




    /**
     * 添加购物车
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        /*
        * 如果购物车中已经有该菜品，则更新数量
        * 每个用户都有自己的购物车，所以以用户id作为条件进行查询
        * 不可能查询到多个相同的菜品，因为如果是同一个菜品，应该更新的数量
        * */
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId)
                .eq(dishId != null, ShoppingCart::getDishId, dishId)
                .eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);
        ShoppingCart cart = shoppingCartMapper.selectOne(queryWrapper);

        // 如果购物车中已经有该菜品，则更新数量
        if (cart != null) {
            // 更新数量
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateById(cart);
            return;
        }

        // 新增逻辑
        // 根据这是菜品还是套餐，从数据库中获取对应的信息
        if (dishId != null) {
            Dish dish = dishMapper.selectById(dishId);
            cart = ShoppingCart.builder()
                    .dishId(dishId)
                    .setmealId(null)
                    .name(dish.getName())
                    .image(dish.getImage())
                    .dishFlavor(shoppingCartDTO.getDishFlavor())
                    .amount(dish.getPrice())
                    .build();
        } else {
            Setmeal setmeal = setmealMapper.selectById(setmealId);
            cart = ShoppingCart.builder()
                    .dishId(null)
                    .setmealId(setmealId)
                    .name(setmeal.getName())
                    .image(setmeal.getImage())
                    .dishFlavor(null)
                    .amount(setmeal.getPrice())
                    .build();
        }

        // 相同的字段统一赋值，然后插入到数据库中
        cart.setUserId(userId);
        cart.setNumber(1);
        shoppingCartMapper.insert(cart);
    }


    /**
     * 查看当前用户的的购物车列表
     */
    @Override
    public List<ShoppingCart> shoppingCartList() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId);
        return shoppingCartMapper.selectList(queryWrapper);
    }


    /**
     * 清空当前登录用户的购物车
     */
    @Override
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId);
        shoppingCartMapper.delete(queryWrapper);
    }


    /**
     * 删除购物车中的一条数据
     */
    @Override
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long userId = BaseContext.getCurrentId();
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<ShoppingCart>()
                .eq(ShoppingCart::getUserId, userId)
                .eq(dishId != null, ShoppingCart::getDishId, dishId)
                .eq(setmealId != null, ShoppingCart::getSetmealId, setmealId);
        ShoppingCart cart = shoppingCartMapper.selectOne(queryWrapper);

        // 如果购物车中已经有该菜品，则更新数量
        if (cart != null) {
            if (cart.getNumber() > 1) {                      // 当前商品在购物车中的份数不为1，修改份数即可
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateById(cart);
            } else {                                        // 当前商品在购物车中的份数为1，直接删除该条数据
                shoppingCartMapper.deleteById(cart.getId());
            }
        }
    }
}
