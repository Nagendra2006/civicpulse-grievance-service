package com.example.GrievanceService.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.example.GrievanceService.workflow.entity.MediaType;

@Entity
@Table(name = "grievance_media")
public class GrievanceMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileUrl;

    // 🔥 ENUM (FIXED)
    @Enumerated(EnumType.STRING)
    private MediaType type;

    @ManyToOne
    @JoinColumn(name = "grievance_id", nullable = false)
    private Grievance grievance;

    private LocalDateTime uploadedAt;

    // 🔥 AUTO TIMESTAMP
    @PrePersist
    public void prePersist() {
        this.uploadedAt = LocalDateTime.now();
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() {
        return id;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public Grievance getGrievance() {
        return grievance;
    }

    public void setGrievance(Grievance grievance) {
        this.grievance = grievance;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
}