package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.client.AuthClient;
import com.example.GrievanceService.client.NotificationClient;
import com.example.GrievanceService.dto.NotificationRequest;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.workflow.entity.Assignment;
import com.example.GrievanceService.workflow.entity.GrievanceHistory;
import com.example.GrievanceService.workflow.repository.AssignmentRepository;
import com.example.GrievanceService.workflow.repository.GrievanceHistoryRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import com.example.GrievanceService.entity.GrievanceMedia;
import com.example.GrievanceService.workflow.entity.MediaType;
import com.example.GrievanceService.repository.GrievanceMediaRepository;
import com.example.GrievanceService.service.AzureStorageService;

@Service
public class StatusService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceHistoryRepository historyRepo;

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private AzureStorageService azureStorageService;

    @Autowired
    private GrievanceMediaRepository mediaRepo;

    public String updateStatus(Long grievanceId,
            String newStatusStr,
            String remarks,
            MultipartFile file,
            String url,
            Long userId) {

        // 🔹 FETCH
        Grievance g = grievanceRepo.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));

        // 🔹 CHECK ASSIGNMENT
        List<Assignment> assignments = assignmentRepo.findByGrievanceIdOrderByIdDesc(grievanceId);

        if (assignments.isEmpty()) {
            throw new RuntimeException("Grievance not assigned yet");
        }

        Assignment assignment = assignments.get(0);

        // 🔹 SECURITY
        if (!assignment.getOfficerId().equals(userId)) {
            throw new RuntimeException("Not authorized");
        }

        // 🔹 ENUM
        GrievanceStatus newStatus;
        try {
            newStatus = GrievanceStatus.valueOf(newStatusStr.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Invalid status");
        }

        GrievanceStatus currentStatus = g.getStatus();

        // 🔹 VALIDATION
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Invalid transition: " + currentStatus + " → " + newStatus);
        }

        // 🔹 UPDATE
        g.setStatus(newStatus);
        grievanceRepo.save(g);

        // 🔹 HISTORY
        saveHistory(grievanceId, newStatus, userId, remarks);

        // 🔹 MEDIA (If RESOLVED)
        if (newStatus == GrievanceStatus.RESOLVED) {
            String finalUrl = null;
            if (file != null && !file.isEmpty()) {
                finalUrl = azureStorageService.uploadFile(file);
            } else if (url != null && !url.trim().isEmpty()) {
                finalUrl = url;
            }
            
            if (finalUrl != null) {
                GrievanceMedia media = new GrievanceMedia();
                media.setFileUrl(finalUrl);
                media.setType(MediaType.RESOLUTION);
                media.setGrievance(g);
                mediaRepo.save(media);
            }
        }

        // 🔥 NOTIFICATION
        sendNotification(g, newStatus);

        return "Status updated to " + newStatus;
    }

    // 🔥 NOTIFICATION METHOD
    private void sendNotification(Grievance g, GrievanceStatus status) {

        // ⚠️ TEMP: replace later with real email fetch
        String email;
        try {
            email = authClient.getUserById(g.getUserId()).getEmail();
        } catch (Exception e) {
            e.printStackTrace();
            return; // don't break flow
        }

        String eventType = switch (status) {
            case ASSIGNED -> "GRIEVANCE_ASSIGNED";
            case IN_PROGRESS -> "GRIEVANCE_IN_PROGRESS";
            case RESOLVED -> "GRIEVANCE_RESOLVED";
            default -> null;
        };

        if (eventType == null)
            return;

        NotificationRequest req = new NotificationRequest();
        req.setEventType(eventType);
        req.setRecipientEmail(email);

        Map<String, Object> data = new HashMap<>();
        data.put("title", g.getTitle());
        data.put("status", status.name());

        req.setData(data);

        try {
            notificationClient.sendNotification(req);
        } catch (Exception e) {
            e.printStackTrace(); // do not break main flow
        }
    }

    private boolean isValidTransition(GrievanceStatus current,
            GrievanceStatus next) {

        if (current == next) return true;

        return switch (current) {
            case PENDING -> next == GrievanceStatus.ASSIGNED;
            case ASSIGNED -> next == GrievanceStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == GrievanceStatus.RESOLVED;
            case RESOLVED -> false;
            case REOPENED -> next == GrievanceStatus.ASSIGNED;
        };
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