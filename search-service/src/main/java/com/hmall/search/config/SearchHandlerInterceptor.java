package com.hmall.search.config;

import ch.qos.logback.classic.sift.JNDIBasedContextDiscriminator;
import com.hmall.common.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Slf4j
@Configuration
public class SearchHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 authorization 信息
        String authorizationHeader = request.getHeader("authorization");

        // 解析 authorization 信息，获取 userId

        // 将 userId 存储到 ThreadLocal 中
        BaseContext.setCurrentId(Long.valueOf(authorizationHeader));
        log.info("Search服务——用户Id：{}",authorizationHeader);

        // 返回 true 表示继续执行后续的拦截器和处理器
        return true;
    }
}
