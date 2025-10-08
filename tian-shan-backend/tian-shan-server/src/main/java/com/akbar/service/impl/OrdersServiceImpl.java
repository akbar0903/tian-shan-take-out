package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.context.BaseContext;
import com.akbar.dto.OrdersSubmitDTO;
import com.akbar.entity.AddressBook;
import com.akbar.entity.OrderDetail;
import com.akbar.entity.Orders;
import com.akbar.entity.ShoppingCart;
import com.akbar.exception.AddressBookBusinessException;
import com.akbar.exception.ShoppingCartBusinessException;
import com.akbar.mapper.AddressBookMapper;
import com.akbar.mapper.OrderDetailMapper;
import com.akbar.mapper.OrdersMapper;
import com.akbar.mapper.ShoppingCartMapper;
import com.akbar.service.OrdersService;
import com.akbar.vo.OrdersSubmitVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;


    /**
     * 用户下单
     * 需要对订单表和订单明细表进行操作
     */
    @Transactional
    @Override
    public OrdersSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        Long userId = BaseContext.getCurrentId();

        // 各种业务异常（收货地址为空，购物车数据为空）
        AddressBook addressBook = addressBookMapper.selectById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        // 检查当前用户的购物车是否为空
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(
                new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId)
        );
        if (shoppingCarts == null || shoppingCarts.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 往订单表插入一条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        // 手动设置订单属性
        orders.setUserId(userId);
        orders.setConsignee(addressBook.getConsignee());     // 收货人
        orders.setPhone(addressBook.getPhone());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));

        // 保存订单
        ordersMapper.insert(orders);
        Long orderId = orders.getId();

        // 往订单明细表插入n条数据，根据购物车中的数据进行插入
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orderId);
            orderDetails.add(orderDetail);
        }
        // 批量插入
        orderDetailMapper.insert(orderDetails);

        // 下单成功之后，清空购物车中的数据
        shoppingCartMapper.delete(new LambdaQueryWrapper<ShoppingCart>().eq(ShoppingCart::getUserId, userId));

        // 返回订单信息
        return OrdersSubmitVO.builder()
                .id(orderId)
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }
}
