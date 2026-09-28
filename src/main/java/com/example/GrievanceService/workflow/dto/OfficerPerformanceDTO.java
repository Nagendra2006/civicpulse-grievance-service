package com.example.GrievanceService.workflow.dto;

public class OfficerPerformanceDTO {

    private Long officerId;
    private Long totalGrievances;
    private Long totalFeedbacks;
    private Double averageRating;
    public Long getOfficerId() {
        return officerId;
    }
    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }
    public Long getTotalGrievances() {
        return totalGrievances;
    }
    public void setTotalGrievances(Long totalGrievances) {
        this.totalGrievances = totalGrievances;
    }
    public Long getTotalFeedbacks() {
        return totalFeedbacks;
    }
    public void setTotalFeedbacks(Long totalFeedbacks) {
        this.totalFeedbacks = totalFeedbacks;
    }
    public Double getAverageRating() {
        return averageRating;
    }
    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    

    // getters & setters
}