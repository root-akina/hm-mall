package com.hmall.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.Address;
import com.hmall.common.dto.Item;
import com.hmall.common.fegin.FeignAddressClient;
import com.hmall.common.fegin.FeignItemClient;
import com.hmall.order.mapper.OrderDetailMapper;
import com.hmall.order.mapper.OrderMapper;
import com.hmall.order.pojo.Order;
import com.hmall.order.pojo.OrderDTO;
import com.hmall.order.pojo.OrderDetail;
import com.hmall.order.service.IOrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private FeignItemClient itemClient;
    @Autowired
    private FeignAddressClient addressClient;



    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper detailMapper;

    /**
     *  订单
     * @param orderDTO 前端数据
     * @return
     */
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

        //支付订单关闭时间
        Date date = new Date();
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime newDateTime = localDateTime.plusSeconds(300);
        Date newDate = Date.from(newDateTime.atZone(ZoneId.systemDefault()).toInstant());

        order.setEndTime(newDate);
        order.setPayTime(newDate);
        order.setCloseTime(newDate);


        //根据地址id找用户
        Address addressById = addressClient.findAddressById(orderDTO.getAddressId());
        order.setUserId(addressById.getUserId());

        order.setCreateTime(new Date());
        //- 6）将Order写入数据库tb_order表中
        int insert = orderMapper.insert(order);
        return order;
    }

    /**
     *  订单细节
     * @param order 订单详情
     * @param orderDTO 前端
     */
    @Override
    public void confirmOrderDetail(Order order, OrderDTO orderDTO) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(order.getId());
        orderDetail.setCreateTime(order.getCreateTime());
        orderDetail.setNum(orderDTO.getNum());

        Item item = itemClient.getItem(orderDTO.getItemId());
        orderDetail.setImage(item.getImage());
        orderDetail.setSpec(item.getSpec());
        orderDetail.setPrice(item.getPrice());
        orderDetail.setTitle(item.getName());
        orderDetail.setItemId(item.getId());
        orderDetail.setUpdateTime(new Date());
    }
}
