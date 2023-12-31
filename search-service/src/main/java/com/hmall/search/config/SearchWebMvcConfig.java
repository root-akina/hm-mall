package com.hmall.search.config;

import com.hmall.common.config.FeignRequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SearchWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private SearchHandlerInterceptor handlerInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor).addPathPatterns("/**");
    }
}
