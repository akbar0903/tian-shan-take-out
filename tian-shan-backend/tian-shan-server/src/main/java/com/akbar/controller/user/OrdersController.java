package com.akbar.controller.user;

import com.akbar.dto.OrdersSubmitDTO;
import com.akbar.result.Result;
import com.akbar.service.OrdersService;
import com.akbar.vo.OrdersSubmitVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Tag(name = "用户端订单相关接口")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;


    /**
     * 用户下单
     */
    @PostMapping("/submit")
    @Operation(summary = "用户下单")
    public Result<OrdersSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO) {
        log.info("用户下单：{}", ordersSubmitDTO);
        OrdersSubmitVO ordersSubmitVO = ordersService.submit(ordersSubmitDTO);
        return Result.success(ordersSubmitVO);
    }
}
