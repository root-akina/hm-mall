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

    /**
     * 订单细节
     * @param order 订单详情
     * @param orderDTO 前端
     */
    void confirmOrderDetail(Order order, OrderDTO orderDTO);

    /**
     * 订单地址
     * @param id 订单Id
     * @param orderDTO 地址表id
     */
    void confirmAddress(Long id, OrderDTO orderDTO);
}
