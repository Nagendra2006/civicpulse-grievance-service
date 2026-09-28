package com.example.GrievanceService.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.client.NotificationClient;
import com.example.GrievanceService.dto.CategoryResponse;
import com.example.GrievanceService.dto.GrievanceRequest;
import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.dto.NotificationRequest;
import com.example.GrievanceService.entity.Category;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.entity.GrievanceMedia;
import com.example.GrievanceService.repository.CategoryRepository;
import com.example.GrievanceService.repository.GrievanceMediaRepository;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.workflow.entity.MediaType;

import org.springframework.web.multipart.MultipartFile;

@Service
public class GrievanceService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private GrievanceMediaRepository mediaRepo;

    @Autowired
    private AzureStorageService azureStorageService;

    @Autowired
    private NotificationClient notificationClient;

    public GrievanceResponse createGrievance(GrievanceRequest request,
            MultipartFile file,
            Long userId, String email) {

        // 🔹 Validate category
        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getParent() == null) {
            throw new RuntimeException("Please select subcategory only");
        }

        // 🔹 Create grievance
        Grievance g = new Grievance();
        g.setUserId(userId);
        g.setCategory(category);
        g.setTitle(request.getTitle());
        g.setDescription(request.getDescription());
        g.setAddress(request.getAddress());
        g.setMandalId(request.getMandalId());

        grievanceRepo.save(g);

        // 🔹 Upload image (if exists)
        if (file != null && !file.isEmpty()) {

            String fileUrl = azureStorageService.uploadFile(file);

            GrievanceMedia media = new GrievanceMedia();
            media.setFileUrl(fileUrl);
            media.setType(MediaType.BEFORE);
            media.setGrievance(g);

            mediaRepo.save(media);
        }
        NotificationRequest req = new NotificationRequest();
        req.setEventType("GRIEVANCE_CREATED");
        req.setRecipientEmail(email);

        Map<String, Object> data = new HashMap<>();
        data.put("title", g.getTitle());

        req.setData(data);

        try {
            notificationClient.sendNotification(req);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Notification failed but continuing...");
        }

        return mapToDTO(g);
    }

    private CategoryResponse mapCategory(Category category) {

        CategoryResponse dto = new CategoryResponse();
        dto.setId(category.getId());
        dto.setName(category.getName());

        return dto;
    }

    private GrievanceResponse mapToDTO(Grievance g) {

        GrievanceResponse dto = new GrievanceResponse();

        dto.setId(g.getId());
        dto.setUserId(g.getUserId());
        dto.setMandalId(g.getMandalId());
        dto.setTitle(g.getTitle());
        dto.setDescription(g.getDescription());
        dto.setAddress(g.getAddress());
        dto.setPriority(g.getPriority());
        dto.setStatus(g.getStatus().name());

        // 🔥 SAFE CATEGORY MAPPING
        CategoryResponse cDto = new CategoryResponse();
        cDto.setId(g.getCategory().getId());
        cDto.setName(g.getCategory().getName());

        dto.setCategory(cDto);

        return dto;
    }
}