package com.example.GrievanceService.workflow.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.workflow.dto.StatusRequest;
import com.example.GrievanceService.workflow.service.StatusService;

@RestController
@RequestMapping("/api/workflow/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @RequestMapping(value = "/{id}", method = {RequestMethod.PUT, RequestMethod.POST})
    @PreAuthorize("hasRole('OFFICER')")
    public String updateStatus(
            @PathVariable Long id,
            @ModelAttribute @Valid StatusRequest request,
            HttpServletRequest httpRequest) {

        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        Long userId = (Long) httpRequest.getAttribute("userId");

        return statusService.updateStatus(
                id,
                request.getStatus(),
                request.getRemarks(),
                request.getFile(),
                request.getUrl(),
                userId);
    }
}