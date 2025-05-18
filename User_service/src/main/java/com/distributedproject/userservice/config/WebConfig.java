package com.distributedproject.userservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Autowired
    private TokenValidationFilter tokenValidationFilter;

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> loggingFilter() {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenValidationFilter);

        // Register all protected endpoint patterns here
        registrationBean.addUrlPatterns(
                "/get/users/*",
                "/getAll/users/*",
                "/get/users/search",
                "/update/users/*",
                "/delete/users/*",
                "/create/users/*",
                "/departments/*",
                "/roles/*"
        );

        return registrationBean;
    }
}
