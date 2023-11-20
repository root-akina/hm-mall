package com.hmall.order.config;

import com.hmall.common.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Configuration
public class OrderHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 authorization 信息
        String authorizationHeader = request.getHeader("authorization");
        String feignHeader = request.getHeader("feignHeader");

        // 解析 authorization 信息，获取 userId

        // 将 userId 存储到 ThreadLocal 中

        if (authorizationHeader != null){
            Long id = Long.valueOf(authorizationHeader);
            BaseContext.setCurrentId(id);
            log.info("Order服务—用户id：{}",id);
        }else if (feignHeader!= null){
            Long feginId = Long.valueOf(feignHeader);
            BaseContext.setCurrentId(feginId);
            log.info("Order服务-feign调用—用户id：{}",feginId);
        }
        else {
            log.error("没有请求头");
        }


        // 返回 true 表示继续执行后续的拦截器和处理器
        return true;
    }
}
