package com.hmall.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignRequestConfig implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header("authorization","78");
    }
}

