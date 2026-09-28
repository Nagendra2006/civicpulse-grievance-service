package com.example.GrievanceService.workflow.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long grievanceId; // 🔥 no direct entity
    private Long officerId; // from auth service 

    @Enumerated(EnumType.STRING)
    private Priority priority;



    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getGrievanceId() {
        return grievanceId;
    }
    public void setGrievanceId(Long grievanceId) {
        this.grievanceId = grievanceId;
    }
    public Long getOfficerId() {
        return officerId;
    }
    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    private String deadline;
    
    public String getDeadline() {
        return deadline;
    }
    
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    

    // getters/setters
}