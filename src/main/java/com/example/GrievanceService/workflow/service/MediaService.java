package com.example.GrievanceService.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.entity.GrievanceMedia;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.service.AzureStorageService;
import com.example.GrievanceService.repository.GrievanceMediaRepository;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.workflow.entity.MediaType;

@Service
public class MediaService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceMediaRepository mediaRepo;

    @Autowired
    private AzureStorageService azureStorageService;

    public String uploadMedia(Long grievanceId,
            MultipartFile file,
            String typeStr,
            Long officerId) {

        Grievance g = grievanceRepo.findById(grievanceId)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is required");
        }

        MediaType type;
        try {
            type = MediaType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid media type");
        }

        // 🔥 Upload to Azure
        String fileUrl = azureStorageService.uploadFile(file);

        // 🔥 Save media
        GrievanceMedia media = new GrievanceMedia();
        media.setFileUrl(fileUrl);
        media.setType(type);
        media.setGrievance(g);

        mediaRepo.save(media);

        return "Media uploaded successfully";
    }
}