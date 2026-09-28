package com.example.GrievanceService.dto;

public class MediaResponse {
    private Long id;
    private String fileUrl;
    private String type;

    public MediaResponse(Long id, String fileUrl, String type) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
