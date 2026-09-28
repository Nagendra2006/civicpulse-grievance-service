package com.example.GrievanceService.workflow.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.GrievanceService.workflow.service.MediaService;

@RestController
@RequestMapping("/api/workflow/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    // 🔥 OFFICER uploads resolution/after image, CITIZEN uploads evidence
    @PreAuthorize("hasAnyRole('OFFICER', 'CITIZEN')")
    @PostMapping("/{id}")
    public String uploadMedia(
            @PathVariable Long id,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        Long officerId = (Long) request.getAttribute("userId");

        return mediaService.uploadMedia(id, file, type, officerId);
    }
}