package com.example.GrievanceService.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.GrievanceService.workflow.entity.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByGrievanceId(Long grievanceId);
    List<Assignment> findByGrievanceIdOrderByIdDesc(Long grievanceId);
    List<Assignment> findByOfficerId(Long officerId);
}

