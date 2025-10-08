package com.akbar.service;

import com.akbar.dto.OrdersSubmitDTO;
import com.akbar.entity.Orders;
import com.akbar.vo.OrdersSubmitVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface OrdersService extends IService<Orders> {
    OrdersSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
}
