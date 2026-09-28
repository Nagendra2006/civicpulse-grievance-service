package com.example.GrievanceService.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.GrievanceService.config.FeignConfig;
import com.example.GrievanceService.dto.NotificationRequest;

@FeignClient(name = "notification-service", url = "${NOTIFICATION_SERVICE_URL:http://localhost:8083}", configuration = FeignConfig.class)
public interface NotificationClient {

    @PostMapping("/api/notification/send")
    void sendNotification(@RequestBody NotificationRequest request);
}