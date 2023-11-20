package com.hmall.search.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        template.header("feignHeader","7");
    }
}
