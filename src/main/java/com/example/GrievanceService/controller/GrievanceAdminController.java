package com.example.GrievanceService.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.service.GrievanceAdminService;

@RestController
@RequestMapping("/api/grievance/admin")
public class GrievanceAdminController {

    @Autowired
    private GrievanceAdminService adminService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/pending")
    public List<GrievanceResponse> getPending(HttpServletRequest request) {
        Long districtId = (Long) request.getAttribute("districtId");
        return adminService.getPendingGrievances(districtId);
    }
}