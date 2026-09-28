package com.example.GrievanceService.workflow.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.workflow.dto.ReopenRequest;
import com.example.GrievanceService.workflow.service.ReopenService;

@RestController
@RequestMapping("/api/workflow/reopen")
public class ReopenController {

    @Autowired
    private ReopenService reopenService;

    // 🔥 ONLY CITIZEN
    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping
    public String reopen(@RequestBody ReopenRequest request,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");

        return reopenService.reopen(
                request.getGrievanceId(),
                request.getReason(),
                userId);
    }
}