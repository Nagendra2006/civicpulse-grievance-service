package com.example.GrievanceService.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.GrievanceService.workflow.entity.GrievanceHistory;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;

public interface GrievanceHistoryRepository
        extends JpaRepository<GrievanceHistory, Long> {

    List<GrievanceHistory> findByGrievanceIdOrderByUpdatedAtAsc(Long grievanceId);

    long countByGrievanceIdAndStatus(Long grievanceId, GrievanceStatus status);
}