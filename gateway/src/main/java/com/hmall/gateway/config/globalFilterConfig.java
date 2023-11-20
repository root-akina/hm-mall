package com.hmall.gateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class globalFilterConfig {

    @Bean
    public GlobalFilter customGlobalFilter(){
        return (exchange, chain) -> {
            // 添加自定义请求头
            exchange.getRequest().mutate().header("authorization", "2").build();

            // 调用下一个过滤器
            return chain.filter(exchange);
        };
    }
}
