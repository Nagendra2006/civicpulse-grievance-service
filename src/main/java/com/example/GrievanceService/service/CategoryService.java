package com.example.GrievanceService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.dto.CategoryResponse;
import com.example.GrievanceService.entity.Category;
import com.example.GrievanceService.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepo;

    // 🔥 Get full hierarchy
    public List<CategoryResponse> getAllCategories() {

        return categoryRepo.findByParentIsNull()
                .stream()
                .map(this::mapToResponse) // ✅ FIXED
                .collect(Collectors.toList());
    }

    private CategoryResponse mapToResponse(Category category) {

        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());

        if (category.getSubCategories() != null) {
            response.setSubCategories(
                    category.getSubCategories()
                            .stream()
                            .map(this::mapToResponse)
                            .collect(Collectors.toList()));
        }

        return response;
    }

    // 🔹 Get subcategories
    public List<CategoryResponse> getSubCategories(Long parentId) {

        categoryRepo.findById(parentId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return categoryRepo.findByParentId(parentId)
                .stream()
                .map(c -> {
                    CategoryResponse res = new CategoryResponse();
                    res.setId(c.getId());
                    res.setName(c.getName());
                    return res;
                })
                .collect(Collectors.toList());
    }
}