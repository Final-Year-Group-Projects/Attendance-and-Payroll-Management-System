package com.example.PayrollService.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FeignClientInterceptorFilter extends OncePerRequestFilter {

    public static final ThreadLocal<String> authHeader = new ThreadLocal<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && !header.isEmpty()) {
            authHeader.set(header);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            authHeader.remove();
        }
    }
}
