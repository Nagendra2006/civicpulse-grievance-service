package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.workflow.repository.GrievanceHistoryRepository;
import com.example.GrievanceService.workflow.entity.GrievanceHistory;

import java.time.LocalDateTime;

@Service
public class ReopenService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceHistoryRepository historyRepo;

    public String reopen(Long grievanceId, String reason, Long userId) {



        Grievance g = grievanceRepo.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));

        long reopenCount = historyRepo.countByGrievanceIdAndStatus(
                grievanceId,
                GrievanceStatus.REOPENED);

        if (reopenCount >= 10) {
            throw new RuntimeException("Reopen limit exceeded (max 10 times)");
        }

        // 🔥 VALIDATION 1: Only RESOLVED
        if (g.getStatus() != GrievanceStatus.RESOLVED) {
            throw new RuntimeException("Only RESOLVED grievances can be reopened");
        }

        // 🔥 VALIDATION 2: Only owner
        if (!g.getUserId().equals(userId)) {
            throw new RuntimeException("You can only reopen your own grievance");
        }

        // 🔥 UPDATE STATUS
        g.setStatus(GrievanceStatus.REOPENED);
        grievanceRepo.save(g);

        // 🔥 SAVE HISTORY
        GrievanceHistory h = new GrievanceHistory();
        h.setGrievanceId(grievanceId);
        h.setStatus(GrievanceStatus.REOPENED);
        h.setUpdatedBy(userId);
        h.setRemarks("Reopened: " + reason);
        h.setUpdatedAt(LocalDateTime.now());

        historyRepo.save(h);

        return "Grievance reopened successfully";
    }
}