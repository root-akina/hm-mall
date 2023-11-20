package com.hmall.order;

import com.hmall.common.fegin.FeignAddressClient;
import com.hmall.common.fegin.FeignItemClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {FeignItemClient.class, FeignAddressClient.class})
@MapperScan("com.hmall.order.mapper")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}