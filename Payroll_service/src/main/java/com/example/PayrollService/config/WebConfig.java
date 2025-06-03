package com.example.PayrollService.config;

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

        // Admin endpoints
        registrationBean.addUrlPatterns(
                "/payrolls",
                "/payrolls/*",
                "/payrolls/all",
                "/payrolls/generateAll*",
                "/payrolls/reimbursements/*"
        );

        // User endpoints
        registrationBean.addUrlPatterns(
                "/payrolls/employee/*",
                "/payrolls/*/payslip",
                "/payrolls/*/payslip/pdf",
                "/payrolls/*/notify",
                "/payrolls/reimbursements",
                "/payrolls/reimbursements/employee/*"
        );

        return registrationBean;
    }
}
