package com.hmall.order.pojo;

import lombok.Data;

@Data
public class OrderDTO {
    //购买数量
    private Integer num;

    /**
     * 付款方式：1:微信支付, 2:支付宝支付, 3:扣减余额
     */
    private Integer paymentType;

    //收件人地址ID
    private Long addressId;

    /**
     * 商品id
     */
    private Long itemId;


}
