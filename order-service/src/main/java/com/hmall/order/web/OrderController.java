package com.hmall.order.web;

import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;
import com.hmall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("{id}")
    public Order queryOrderById(@PathVariable("id") Long orderId) {
        return orderService.getById(orderId);
    }

    @PostMapping
    public Long orderConfirm(@RequestBody OrderDTO orderDTO) {
        log.info("创建订单：数量：{},付款方式：{},收货人地址：{},商品Id：{}", orderDTO.getNum(), orderDTO.getPaymentType(), orderDTO.getAddressId(), orderDTO.getItemId());
        Order order = orderService.confirmOrder(orderDTO);
        return order.getId();
    }
}
