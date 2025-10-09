package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.context.BaseContext;
import com.akbar.dto.OrdersPaymentDTO;
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
import com.akbar.vo.OrderPaymentVO;
import com.akbar.vo.OrdersSubmitVO;
import com.akbar.websocket.WebSocketServer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private WebSocketServer webSocketServer;


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


    /**
     * 订单支付
     */
    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        Long userId = BaseContext.getCurrentId();
        String orderNumber = ordersPaymentDTO.getOrderNumber();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getNumber, orderNumber)
                .eq(Orders::getUserId, userId);
        Orders order = ordersMapper.selectOne(queryWrapper);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 如果订单已经支付，直接返回
        if (order.getPayStatus() != null && order.getPayStatus() == Orders.PAID) {
            throw new RuntimeException("订单已支付");
        }

        // ==========================
        // 3. 模拟支付成功
        // ==========================
        // 实际项目这里是调用 weChatPayUtil.pay() 向微信下单
        // 我们这里直接认为支付成功，然后走 paySuccess() 逻辑
        paySuccess(orderNumber);

        // ==========================
        // 4. 返回模拟的支付信息（格式与真实微信支付保持一致）
        // ==========================
        // 生成模拟数据
        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        String paySign = "mock_sign_" + System.currentTimeMillis();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String packageStr = "prepay_id=mock_prepay_" + (int) (Math.random() * 1000000);
        String signType = "RSA";

        return OrderPaymentVO.builder()
                .nonceStr(nonceStr)
                .paySign(paySign)
                .timeStamp(timeStamp)
                .signType(signType)
                .packageStr(packageStr)
                .build();
    }


    /**
     * 工具方法
     * 支付成功后调用的方法（模拟微信支付回调）
     */
    public void paySuccess(String orderNumber) {
        Long userId = BaseContext.getCurrentId();

        // 根据订单号和用户ID查询订单
        Orders order = ordersMapper.selectOne(
                new LambdaQueryWrapper<Orders>()
                       .eq(Orders::getNumber, orderNumber)
                       .eq(Orders::getUserId, userId)
        );
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 更新订单的状态信息
        order.setPayStatus(Orders.PAID);            // 支付状态：已支付
        order.setStatus(Orders.TO_BE_CONFIRMED);    // 订单状态：待接单
        order.setCheckoutTime(LocalDateTime.now()); // 订单完成时间
        ordersMapper.updateById(order);

        // 向后台管理端发送WebSocket通知
        Map<String, Object> message = new HashMap<>();
        message.put("type", 1);
        message.put("orderId", order.getId());
        message.put("content", "订单号：" + order.getNumber());

        // 群发消息
        webSocketServer.sendToAllClient(JSON.toJSONString(message));
    }
}
