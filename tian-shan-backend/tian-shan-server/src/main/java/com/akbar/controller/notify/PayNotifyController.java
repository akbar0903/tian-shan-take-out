package com.akbar.controller.notify;


import com.akbar.properties.WeChatProperties;
import com.akbar.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 支付回调相关接口
 */
@RestController
@RequestMapping("/notify")
@Slf4j
public class PayNotifyController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private WeChatProperties weChatProperties;
}
