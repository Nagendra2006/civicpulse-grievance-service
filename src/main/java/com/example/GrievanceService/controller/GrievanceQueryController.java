package com.example.GrievanceService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.common.ApiResponse;
import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.service.GrievanceQueryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/grievances")
public class GrievanceQueryController {

    @Autowired
    private GrievanceQueryService service;

    @GetMapping
    public ApiResponse<Page<GrievanceResponse>> getGrievances(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long mandalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        
        Long districtId = (Long) request.getAttribute("districtId");
        Long userId = (Long) request.getAttribute("userId");
        
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isCitizen = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CITIZEN"));
        boolean isOfficer = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_OFFICER"));
        
        Long filterUserId = isCitizen ? userId : null;
        Long officerId = isOfficer ? userId : null;
        
        var result = service.getGrievances(page, size, status, categoryId, mandalId, districtId, filterUserId, officerId, fromDate, toDate);
        return new ApiResponse(true, "Grievances retrieved successfully", result);
    }

    @GetMapping("/{id}")
    public ApiResponse<GrievanceResponse> getGrievanceById(@PathVariable Long id) {
        var result = service.getGrievanceById(id);
        return new ApiResponse(true, "Grievance retrieved successfully", result);
    }

    @GetMapping("/export/excel")
    public void exportToExcel(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long mandalId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) throws Exception {

        Long districtId = (Long) request.getAttribute("districtId");
        Long userId = (Long) request.getAttribute("userId");
        
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean isOfficer = auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_OFFICER"));
        Long officerId = isOfficer ? userId : null;
        
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"grievances.csv\"");

        List<GrievanceResponse> list = service.getAllGrievancesForExport(status, categoryId, mandalId, districtId, officerId, fromDate, toDate);

        PrintWriter writer = response.getWriter();
        writer.println("ID,Title,Description,Status,Priority,Category");
        for (GrievanceResponse g : list) {
            String title = g.getTitle() != null ? g.getTitle().replace(",", " ") : "";
            String desc = g.getDescription() != null ? g.getDescription().replace(",", " ").replace("\n", " ") : "";
            String categoryName = g.getCategory() != null ? g.getCategory().getName() : "";
            writer.println(g.getId() + "," + title + "," + desc + "," + g.getStatus() + "," + g.getPriority() + "," + categoryName);
        }
    }
}