package com.hmall.item;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
public class SpringMQTest {

    @Autowired
    private RabbitTemplate rabbit;

    @Test
    public void publisher(){
// 队列名称
        String queueName = "simple.queue";
        // 消息
        String message = "hello, spring amqp!";
        // 发送消息
        rabbit.convertAndSend(queueName, message);
    }

    @Test
    public void itemPublisher(){
// 队列名称
        String queueName = "item.status";
        // 消息
        Map<String, Object> message = new HashMap<>();
        message.put("status",1);
        message.put("id",561178);

        // 发送消息
        rabbit.convertAndSend(queueName, message);
    }
}
