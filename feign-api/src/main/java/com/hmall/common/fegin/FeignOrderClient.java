package com.hmall.common.fegin;

import com.hmall.common.config.FeignRequestConfig;
import com.hmall.common.dto.Order;
import com.hmall.common.dto.OrderDetail;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "orderservice",path = "order",configuration = FeignRequestConfig.class)
public interface FeignOrderClient {

    //根据id查询Order
    @GetMapping("{id}")
    Order queryOrderById(@PathVariable("id") String  orderId);
}
