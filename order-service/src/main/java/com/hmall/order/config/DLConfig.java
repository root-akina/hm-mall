package com.hmall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DLConfig {
    @Bean
    public DirectExchange ttlExchange(){
        return new DirectExchange("ttl.direct");
    }

    @Bean
    public Queue ttlQueue(){
        return QueueBuilder
                .durable("ttl.queue")
                .ttl(20000)
                .deadLetterExchange("dl.direct")
                .deadLetterRoutingKey("dl")
                .build();
    }

    @Bean
    public Binding binding(){
        return BindingBuilder.bind(ttlQueue()).to(ttlExchange()).with("ttl");
    }
}
