package com.example.GrievanceService.workflow.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.workflow.dto.AssignRequest;
import com.example.GrievanceService.workflow.service.AssignmentService;

@RestController
@RequestMapping("/api/workflow/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    // 🔥 ADMIN assigns grievance with priority
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String assignGrievance(@RequestBody @Valid AssignRequest request,
            HttpServletRequest httpRequest) {

        // 🔹 Extract adminId from JWT (set in JwtFilter)
        Long adminId = (Long) httpRequest.getAttribute("userId");

        // 🔹 fallback (for testing if JWT doesn't have userId yet)
        if (adminId == null) {
            adminId = 1L;
        }

        String email = (String) httpRequest.getAttribute("email");

        return assignmentService.assign(request, adminId, email);
    }
}