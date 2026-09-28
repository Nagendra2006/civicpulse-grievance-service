package com.example.GrievanceService.dto;


public class DistrictResponse {

    private Long id;
    private String name;

    public DistrictResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getters
}
