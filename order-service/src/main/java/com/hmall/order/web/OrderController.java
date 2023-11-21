package com.hmall.order.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hmall.common.fegin.FeignItemClient;
import com.hmall.common.fegin.FeignOrderClient;
import com.hmall.order.mapper.OrderDetailMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;


@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private FeignItemClient itemClient;


    @GetMapping("{id}")
    public Order queryOrderById(@PathVariable("id") String orderId) {
        System.out.println(orderId);
        return orderService.getById(orderId);
    }


    @Transactional
    @PostMapping
    public String orderConfirm(@RequestBody OrderDTO orderDTO) {
        log.info("创建订单：数量：{},付款方式：{},收货人地址：{},商品Id：{}", orderDTO.getNum(), orderDTO.getPaymentType(), orderDTO.getAddressId(), orderDTO.getItemId());
        Order order = orderService.confirmOrder(orderDTO);
        orderService.confirmOrderDetail(order, orderDTO);
        orderService.confirmAddress(order.getId(), orderDTO);
        String orderId = String.valueOf(order.getId());
        System.out.println(orderId);
        //发送订单信息，延迟30min
        //1.交换机,key,消息
        String exchange = "ttl.direct";
        String key = "ttl";
        Message ttp = MessageBuilder.withBody(orderId.getBytes(StandardCharsets.UTF_8)).setExpiration("300000").build();
        rabbitTemplate.convertAndSend(exchange, key, ttp);
        return orderId;
    }

    //超时订单
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "dl.queue"),
            exchange = @Exchange(name = "dl.direct"),
            key = "dl"
    ))
    public void timeoutPayment(String msg) {
        if (StringUtils.isEmpty(msg)) {
            log.error("空的RabbitMQ信息");
            return;
        }
        log.error("死信：{}",msg);

        Order order = orderService.getById(msg);
        if (order != null && order.getStatus() == 1) {
            //超时未支付订单
            //获取数量，并修改状态
            //根据订单id获取OD
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
            QueryWrapper<OrderDetail> orderDetailQ = orderDetailQueryWrapper.eq("order_id", order.getId());
            OrderDetail orderDetail = orderDetailMapper.selectOne(orderDetailQ);
            // 5、交易取消
            UpdateWrapper<Order> orderUpdate = new UpdateWrapper<>();
            orderUpdate.set("status", 5).eq("id", orderDetail.getOrderId());

            Integer num = orderDetail.getNum();
            itemClient.stockUpdate(orderDetail.getItemId(), -num);
        }

    }
}
