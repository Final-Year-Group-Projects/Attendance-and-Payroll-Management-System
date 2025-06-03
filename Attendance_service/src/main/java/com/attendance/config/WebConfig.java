package com.attendance.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Autowired
    private TokenValidationFilter tokenValidationFilter;

    @Bean
    public FilterRegistrationBean<TokenValidationFilter> registerTokenFilter() {
        FilterRegistrationBean<TokenValidationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(tokenValidationFilter);

        // All URLs that should be protected by token and role checks
        registrationBean.addUrlPatterns(
                "/attendance/*"
        );

        return registrationBean;
    }
}
