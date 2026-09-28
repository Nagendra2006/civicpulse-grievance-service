package com.example.GrievanceService.common;

import java.time.LocalDateTime;

public class ErrorResponse {

    private boolean success;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}