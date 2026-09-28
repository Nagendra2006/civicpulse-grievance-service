package com.example.GrievanceService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.example.GrievanceService.dto.CategoryResponse;
import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.repository.GrievanceMediaRepository;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.dto.MediaResponse;
import java.util.stream.Collectors;

@Service
public class GrievanceAdminService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceMediaRepository mediaRepo;

    public List<GrievanceResponse> getPendingGrievances(Long districtId) {

        if (districtId != null) {
            return grievanceRepo.findPendingByDistrict(districtId)
                    .stream()
                    .map(this::mapToDTO)
                    .toList();
        }

        // fallback if districtId is somehow missing
        return grievanceRepo.findByStatus(GrievanceStatus.PENDING)
                .stream()
                .map(this::mapToDTO) // ✅ now valid
                .toList();
    }

    // 🔥 ADD THIS METHOD (MISSING)
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

        // 🔥 FETCH AND SET MEDIA
        var mediaList = mediaRepo.findByGrievanceId(g.getId())
                .stream()
                .map(m -> new MediaResponse(m.getId(), m.getFileUrl(), m.getType().name()))
                .collect(Collectors.toList());
        dto.setMedia(mediaList);

        return dto;
    }
}