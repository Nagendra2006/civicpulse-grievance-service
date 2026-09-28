package com.example.GrievanceService.workflow.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.GrievanceService.workflow.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    boolean existsByGrievanceId(Long grievanceId);

    List<Feedback> findByGrievanceId(Long grievanceId);

    @Query("""
                SELECT a.officerId,
                       COUNT(f.id),
                       AVG(f.rating)
                FROM Feedback f
                JOIN Assignment a ON f.grievanceId = a.grievanceId
                GROUP BY a.officerId
            """)
    List<Object[]> getOfficerPerformanceRaw();
}