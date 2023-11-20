package com.hmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.Address;
import com.hmall.common.dto.Item;
import com.hmall.common.fegin.FeignAddressClient;
import com.hmall.common.fegin.FeignItemClient;
import com.hmall.order.mapper.OrderMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;
import com.hmall.order.service.IOrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private FeignItemClient itemClient;

    @Autowired
    private FeignAddressClient addressClient;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order confirmOrder(OrderDTO orderDTO) {
        Order order = new Order();
        //- 根据雪花算法生成订单id
        //(type = IdType.ASSIGN_ID)

        //- 2）商品微服务提供FeignClient，实现根据id查询商品的接口
        //- 3）根据itemId查询商品信息
        Item itemOrder = itemClient.getItem(orderDTO.getItemId());
        //- 4）基于商品价格、购买数量计算商品总价：totalFee
        Long price = itemOrder.getPrice();
        long totalFee = price * orderDTO.getNum();
        order.setTotalFee(totalFee);
        //- 5）封装Order对象，初识status为未支付
        order.setPaymentType(orderDTO.getPaymentType());
        //订单状态,1、未付款 2、已付款,未发货 3、已发货,未确认 4、确认收货，交易成功 5、交易取消，订单关闭 6、交易结束
        order.setStatus(1);

        //根据地址id找用户
        Address addressById = addressClient.findAddressById(orderDTO.getAddressId());
        order.setUserId(addressById.getUserId());

        order.setCreateTime(new Date());
        //- 6）将Order写入数据库tb_order表中
        int insert = orderMapper.insert(order);
        return order;
    }
}
