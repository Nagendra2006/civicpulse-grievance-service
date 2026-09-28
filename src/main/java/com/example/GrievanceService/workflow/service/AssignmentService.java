package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.client.NotificationClient;
import com.example.GrievanceService.dto.NotificationRequest;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.workflow.dto.AssignRequest;
import com.example.GrievanceService.workflow.entity.*;
import com.example.GrievanceService.workflow.repository.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Autowired
    private GrievanceHistoryRepository historyRepo;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private GrievanceRepository grievanceRepo; // 🔥 ADD THIS

    public String assign(AssignRequest request, Long adminId, String userEmail) {

        // 🔥 FETCH GRIEVANCE
        Grievance g = grievanceRepo.findById(request.getGrievanceId())
                .orElseThrow(() -> new RuntimeException("Grievance not found"));

        // 🔥 CONVERT PRIORITY
        Priority priority = Priority.valueOf(request.getPriority().toUpperCase());

        // 🔥 UPDATE MAIN TABLE (MOST IMPORTANT)
        g.setStatus(GrievanceStatus.ASSIGNED);
        g.setPriority(priority);
        grievanceRepo.save(g);

        // 🔥 SAVE ASSIGNMENT
        Assignment a = new Assignment();
        a.setGrievanceId(request.getGrievanceId());
        a.setOfficerId(request.getOfficerId());
        a.setPriority(priority);
        a.setDeadline(request.getDeadline());

        assignmentRepo.save(a);

        // 🔥 SAVE HISTORY
        saveHistory(request.getGrievanceId(),
                GrievanceStatus.ASSIGNED,
                adminId,
                "Assigned with priority " + priority);

        NotificationRequest req = new NotificationRequest();
        req.setEventType("GRIEVANCE_ASSIGNED");
        req.setRecipientEmail(userEmail);

        Map<String, Object> data = new HashMap<>();
        data.put("title", g.getTitle());
        data.put("officer", "Officer Name");

        req.setData(data);

        notificationClient.sendNotification(req);

        return "Assigned successfully with priority";
    }

    private void saveHistory(Long grievanceId,
            GrievanceStatus status,
            Long userId,
            String remarks) {

        GrievanceHistory h = new GrievanceHistory();
        h.setGrievanceId(grievanceId);
        h.setStatus(status);
        h.setUpdatedBy(userId);
        h.setRemarks(remarks);
        h.setUpdatedAt(LocalDateTime.now());

        historyRepo.save(h);
    }
}