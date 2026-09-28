package com.example.GrievanceService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.workflow.entity.Priority;

@Entity
@Table(name = "grievances")
public class Grievance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long mandalId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String title;
    private String description;
    private String address;

    // ENUM (FIXED)
    @Enumerated(EnumType.STRING)
    private Priority priority;

    // ENUM (FIXED)
    @Enumerated(EnumType.STRING)
    private GrievanceStatus status;

    private LocalDateTime createdAt;

    // AUTO SET DEFAULT VALUES
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = GrievanceStatus.PENDING;
        }
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMandalId() {
        return mandalId;
    }

    public void setMandalId(Long mandalId) {
        this.mandalId = mandalId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public GrievanceStatus getStatus() {
        return status;
    }

    public void setStatus(GrievanceStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}