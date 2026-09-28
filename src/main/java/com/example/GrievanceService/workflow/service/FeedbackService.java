package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.workflow.entity.Feedback;
import com.example.GrievanceService.workflow.repository.FeedbackRepository;
import com.example.GrievanceService.workflow.repository.GrievanceHistoryRepository;
import com.example.GrievanceService.workflow.entity.GrievanceHistory;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepo;

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceHistoryRepository historyRepo;

    public String addFeedback(Feedback feedback, Long userId) {

        // 🔹 1. Check grievance exists
        Grievance grievance = grievanceRepo.findById(feedback.getGrievanceId())
                .orElseThrow(() -> new RuntimeException("Grievance not found"));

        // 🔹 2. Check status = RESOLVED
        List<GrievanceHistory> history = historyRepo.findByGrievanceIdOrderByUpdatedAtAsc(feedback.getGrievanceId());

        GrievanceStatus latestStatus = history.get(history.size() - 1).getStatus();

        if (latestStatus != GrievanceStatus.RESOLVED) {
            throw new RuntimeException("Feedback allowed only after resolution");
        }

        // 🔹 3. (Optional) Check owner
        if (!grievance.getUserId().equals(userId)) {
            throw new RuntimeException("You can only give feedback to your own grievance");
        }

        // 🔹 4. (Optional) Prevent duplicate feedback
        if (feedbackRepo.existsByGrievanceId(feedback.getGrievanceId())) {
            throw new RuntimeException("Feedback already submitted");
        }

        // 🔹 Save feedback
        feedback.setUserId(userId);
        feedbackRepo.save(feedback);

        return "Feedback submitted successfully";
    }

    public List<Feedback> getFeedback(Long grievanceId) {
        return feedbackRepo.findByGrievanceId(grievanceId);
    }
}