package com.example.GrievanceService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.GrievanceService.config.FeignConfig;
import com.example.GrievanceService.dto.UserResponse;

@FeignClient(name = "auth-service", url = "${AUTH_SERVICE_URL:http://localhost:8081}", configuration = FeignConfig.class)
public interface AuthClient {

    @GetMapping("/api/auth/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long userId);

    @GetMapping("/api/auth/validate")
    boolean validateToken(@RequestHeader("Authorization") String token);

}