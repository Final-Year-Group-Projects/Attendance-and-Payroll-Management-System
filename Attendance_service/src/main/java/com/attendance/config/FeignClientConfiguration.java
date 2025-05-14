package com.attendance.config;

import feign.Feign;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Feign.Builder feignBuilder(CircuitBreakerRegistry circuitBreakerRegistry) {
        // Use the circuit breaker instance for USER-SERVICE
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("USER-SERVICE");
        FeignDecorators decorators = FeignDecorators.builder()
                .withCircuitBreaker(circuitBreaker)
                .build();
        return Resilience4jFeign.builder(decorators);
    }
}