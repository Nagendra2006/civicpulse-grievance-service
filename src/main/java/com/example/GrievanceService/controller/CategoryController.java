package com.example.GrievanceService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.dto.CategoryResponse;
import com.example.GrievanceService.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get hierarchical categories (major + subcategories)")
    @GetMapping
    public List<CategoryResponse> getCategories() {
        return categoryService.getAllCategories();
    }

    @Operation(summary = "Get subcategories for a major category")
    @GetMapping("/subcategories")
    public List<CategoryResponse> getSubCategories(@RequestParam Long parentId) {
        return categoryService.getSubCategories(parentId);
    }
}
