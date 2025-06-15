package com.example.PayrollService.config;

import com.example.PayrollService.filter.FeignClientInterceptorFilter;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");

            // Get the Authorization header stored in ThreadLocal by the interceptor/filter
            String authHeader = FeignClientInterceptorFilter.authHeader.get();

            if (authHeader != null && !authHeader.isEmpty()) {
                requestTemplate.header("Authorization", authHeader);
            }
        };
    }
}
