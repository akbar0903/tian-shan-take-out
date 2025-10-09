package com.akbar.task;

import com.akbar.entity.Orders;
import com.akbar.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类
 * 定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 处理超时订单
     * 每分钟触发一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeOutOrder() {
        log.info("处理超时订单，当前时间：{}", LocalDateTime.now());

        // 订单下单时间超过15分钟，自动取消订单
        LocalDateTime time = LocalDateTime.now().minusMinutes(15);
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, time);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时未支付，系统自动取消订单");
                order.setCancelTime(LocalDateTime.now());
                ordersMapper.updateById(order);
                log.info("订单：{} 已超时，已自动取消", order.getNumber());
            }
        }
    }


    /**
     * 处理一直处于派送中状态的订单
     * 每天凌晨1点触发一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("处理一直处于派送中状态的订单，当前时间：{}", LocalDateTime.now());

        // 当前时间，也就是凌晨1点减1个小时，也就是凌晨0点,sql是这样order_time < #{orderTime}
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        List<Orders> ordersList = ordersMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, time);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders order : ordersList) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
                ordersMapper.updateById(order);
                log.info("订单：{} 已完成，已自动标记为派送完成", order.getNumber());
            }
        }
    }
}
