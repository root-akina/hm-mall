package com.hmall.order.web;

import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;
import com.hmall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("{id}")
    public Order queryOrderById(@PathVariable("id") String  orderId) {
        System.out.println(orderId);
        return orderService.getById(orderId);
    }

    @Transactional
    @PostMapping
    public String  orderConfirm(@RequestBody OrderDTO orderDTO) {
        log.info("创建订单：数量：{},付款方式：{},收货人地址：{},商品Id：{}", orderDTO.getNum(), orderDTO.getPaymentType(), orderDTO.getAddressId(), orderDTO.getItemId());
        Order order = orderService.confirmOrder(orderDTO);
        orderService.confirmOrderDetail(order,orderDTO);
        orderService.confirmAddress(order.getId(),orderDTO);
        String orderId = String.valueOf(order.getId());
        System.out.println(orderId);
        return orderId;
    }
}
