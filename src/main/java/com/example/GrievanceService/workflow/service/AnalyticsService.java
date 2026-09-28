package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.GrievanceService.workflow.dto.OfficerPerformanceDTO;
import com.example.GrievanceService.workflow.repository.FeedbackRepository;

@Service
public class AnalyticsService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    public List<OfficerPerformanceDTO> getOfficerPerformance() {

        List<Object[]> results = feedbackRepo.getOfficerPerformanceRaw();

        return results.stream().map(obj -> {

            OfficerPerformanceDTO dto = new OfficerPerformanceDTO();

            dto.setOfficerId((Long) obj[0]);
            dto.setTotalFeedbacks((Long) obj[1]);
            dto.setAverageRating((Double) obj[2]);

            // Optional: total grievances = feedback count (simplified)
            dto.setTotalGrievances((Long) obj[1]);

            return dto;

        }).collect(Collectors.toList());
    }
}