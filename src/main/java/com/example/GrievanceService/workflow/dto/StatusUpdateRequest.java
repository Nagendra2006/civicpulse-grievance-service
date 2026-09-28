package com.example.GrievanceService.workflow.dto;

public class StatusUpdateRequest {
    private String status;
    private String remarks;
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // getters/setters
}