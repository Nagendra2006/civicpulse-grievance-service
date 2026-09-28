package com.example.GrievanceService.dto;

import java.util.List;

public class CategoryResponse {

    private Long id;
    private String name;

    // 🔥 ADD THIS BACK
    private List<CategoryResponse> subCategories;

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

    public List<CategoryResponse> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryResponse> subCategories) {
        this.subCategories = subCategories;
    }
}