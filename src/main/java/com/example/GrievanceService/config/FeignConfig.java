package com.example.GrievanceService.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.*;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {

        return requestTemplate -> {

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes == null)
                return;

            HttpServletRequest request = attributes.getRequest();

            String authHeader = request.getHeader("Authorization");

            if (authHeader != null) {
                requestTemplate.header("Authorization", authHeader);
            }
        };
    }
}