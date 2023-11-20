package com.hmall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;

public interface IOrderService extends IService<Order> {


    /**
     * 创建订单
     * @param orderDTO 前端数据
     * @return Long 订单Id
     */
    Order confirmOrder(OrderDTO orderDTO);
}
