package com.example.GrievanceService.dto;

import com.example.GrievanceService.workflow.entity.Priority;
import java.util.List;

public class GrievanceResponse {

    private Long id;
    private Long userId;
    private Long mandalId;

    private String title;
    private String description;
    private String address;

    private Priority priority;
    private String status;

    private CategoryResponse category;
    
    private List<MediaResponse> media;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public List<MediaResponse> getMedia() {
        return media;
    }

    public void setMedia(List<MediaResponse> media) {
        this.media = media;
    }

    private Long officerId;
    private String deadline;

    public Long getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Long officerId) {
        this.officerId = officerId;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    private String resolutionNote;

    public String getResolutionNote() {
        return resolutionNote;
    }

    public void setResolutionNote(String resolutionNote) {
        this.resolutionNote = resolutionNote;
    }
}