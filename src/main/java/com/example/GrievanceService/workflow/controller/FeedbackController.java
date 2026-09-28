package com.example.GrievanceService.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.workflow.entity.Feedback;
import com.example.GrievanceService.workflow.service.FeedbackService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/workflow/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("hasRole('CITIZEN')")
    @PostMapping
    public String submit(@RequestBody Feedback feedback,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");

        return feedbackService.addFeedback(feedback, userId);
    }

    @GetMapping("/{grievanceId}")
    public java.util.List<Feedback> getFeedback(@PathVariable Long grievanceId) {
        return feedbackService.getFeedback(grievanceId);
    }
}