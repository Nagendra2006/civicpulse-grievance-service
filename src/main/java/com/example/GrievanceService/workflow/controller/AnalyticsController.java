package com.example.GrievanceService.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.GrievanceService.workflow.dto.OfficerPerformanceDTO;
import com.example.GrievanceService.workflow.service.AnalyticsService;

@RestController
@RequestMapping("/api/workflow/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    // 🔥 ADMIN only
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/officers")
    public List<OfficerPerformanceDTO> getPerformance() {
        return analyticsService.getOfficerPerformance();
    }
}