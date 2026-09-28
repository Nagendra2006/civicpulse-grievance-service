package com.example.GrievanceService.workflow.dto;

public class ReopenRequest {

    private Long grievanceId;
    private String reason;

    public Long getGrievanceId() {
        return grievanceId;
    }

    public void setGrievanceId(Long grievanceId) {
        this.grievanceId = grievanceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}