package com.example.GrievanceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GrievanceService.entity.GrievanceMedia;
import java.util.List;

public interface GrievanceMediaRepository extends JpaRepository<GrievanceMedia, Long> {
    List<GrievanceMedia> findByGrievanceId(Long grievanceId);
}
