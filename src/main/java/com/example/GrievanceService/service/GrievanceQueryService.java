package com.example.GrievanceService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.repository.GrievanceRepository;
import com.example.GrievanceService.repository.GrievanceMediaRepository;
import com.example.GrievanceService.spec.GrievanceSpecification;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;
import com.example.GrievanceService.dto.MediaResponse;
import com.example.GrievanceService.dto.MandalResponse;
import java.util.stream.Collectors;
import java.util.List;

import java.time.LocalDateTime;

@Service
public class GrievanceQueryService {

    @Autowired
    private GrievanceRepository grievanceRepo;

    @Autowired
    private GrievanceMediaRepository mediaRepo;

    @Autowired
    private LocationService locationService;

    @Autowired
    private com.example.GrievanceService.workflow.repository.AssignmentRepository assignmentRepo;

    @Autowired
    private com.example.GrievanceService.workflow.repository.GrievanceHistoryRepository historyRepo;

    public Page<GrievanceResponse> getGrievances(
            int page,
            int size,
            String status,
            Long categoryId,
            Long mandalId,
            Long districtId,
            Long userId,
            Long officerId,
            LocalDateTime fromDate,
            LocalDateTime toDate) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        GrievanceStatus statusEnum = null;
        if (status != null) {
            statusEnum = GrievanceStatus.valueOf(status.toUpperCase());
        }

        List<Long> districtMandalIds = null;
        if (districtId != null && mandalId == null) {
            districtMandalIds = locationService.getMandals(districtId)
                    .stream()
                    .map(com.example.GrievanceService.dto.MandalResponse::getId)
                    .collect(Collectors.toList());
        }

        List<Long> assignedGrievanceIds = null;
        if (officerId != null) {
            assignedGrievanceIds = assignmentRepo.findByOfficerId(officerId)
                    .stream()
                    .map(com.example.GrievanceService.workflow.entity.Assignment::getGrievanceId)
                    .collect(Collectors.toList());
            if (assignedGrievanceIds.isEmpty()) {
                assignedGrievanceIds.add(-1L);
            }
        }

        Page<Grievance> result = grievanceRepo.findAll(
                com.example.GrievanceService.spec.GrievanceSpecification.filter(statusEnum, categoryId, mandalId, districtMandalIds, userId, assignedGrievanceIds, fromDate, toDate),
                pageable);

        return result.map(this::mapToDTO);
    }

    public GrievanceResponse getGrievanceById(Long id) {
        Grievance g = grievanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Grievance not found"));
        return mapToDTO(g);
    }

    public List<GrievanceResponse> getAllGrievancesForExport(
            String status, Long categoryId, Long mandalId, Long districtId, Long officerId,
            LocalDateTime fromDate, LocalDateTime toDate) {

        GrievanceStatus statusEnum = null;
        if (status != null) {
            statusEnum = GrievanceStatus.valueOf(status.toUpperCase());
        }

        List<Long> districtMandalIds = null;
        if (districtId != null && mandalId == null) {
            districtMandalIds = locationService.getMandals(districtId)
                    .stream()
                    .map(com.example.GrievanceService.dto.MandalResponse::getId)
                    .collect(Collectors.toList());
        }

        List<Long> assignedGrievanceIds = null;
        if (officerId != null) {
            assignedGrievanceIds = assignmentRepo.findByOfficerId(officerId)
                    .stream()
                    .map(com.example.GrievanceService.workflow.entity.Assignment::getGrievanceId)
                    .collect(Collectors.toList());
            if (assignedGrievanceIds.isEmpty()) {
                assignedGrievanceIds.add(-1L);
            }
        }

        List<Grievance> result = grievanceRepo.findAll(
                com.example.GrievanceService.spec.GrievanceSpecification.filter(statusEnum, categoryId, mandalId, districtMandalIds, null, assignedGrievanceIds, fromDate, toDate),
                Sort.by("createdAt").descending());

        return result.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private GrievanceResponse mapToDTO(Grievance g) {
        GrievanceResponse dto = new GrievanceResponse();
        dto.setId(g.getId());
        dto.setTitle(g.getTitle());
        dto.setStatus(g.getStatus().name());
        dto.setPriority(g.getPriority());
        dto.setMandalId(g.getMandalId());

        var c = g.getCategory();
        var cDto = new com.example.GrievanceService.dto.CategoryResponse();
        cDto.setId(c.getId());
        cDto.setName(c.getName());
        dto.setCategory(cDto);

        // Also map missing fields for details view
        dto.setDescription(g.getDescription());
        dto.setAddress(g.getAddress());
        dto.setUserId(g.getUserId());

        // Fetch assignment for officerId and deadline
        var assignments = assignmentRepo.findByGrievanceIdOrderByIdDesc(g.getId());
        if (!assignments.isEmpty()) {
            var latest = assignments.get(0);
            dto.setOfficerId(latest.getOfficerId());
            dto.setDeadline(latest.getDeadline());
        }

        // 🔥 FETCH AND SET MEDIA
        var mediaList = mediaRepo.findByGrievanceId(g.getId())
                .stream()
                .map(m -> new MediaResponse(m.getId(), m.getFileUrl(), m.getType().name()))
                .collect(Collectors.toList());
        dto.setMedia(mediaList);

        // 🔥 FETCH RESOLUTION NOTE
        var history = historyRepo.findByGrievanceIdOrderByUpdatedAtAsc(g.getId());
        history.stream()
                .filter(h -> h.getStatus() == GrievanceStatus.RESOLVED)
                .reduce((first, second) -> second) // get the last resolved status
                .ifPresent(h -> dto.setResolutionNote(h.getRemarks()));

        return dto;
    }
}