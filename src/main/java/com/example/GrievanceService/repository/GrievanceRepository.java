package com.example.GrievanceService.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.workflow.entity.GrievanceStatus;

public interface GrievanceRepository extends JpaRepository<Grievance, Long>, JpaSpecificationExecutor<Grievance>  {

     @Query("""
        SELECT g FROM Grievance g
        JOIN Mandal m ON g.mandalId = m.id
        WHERE m.district.id = :districtId
        AND g.status = 'PENDING'
    """)
    List<Grievance> findPendingByDistrict(Long districtId);


    List<Grievance> findByStatus(GrievanceStatus status);


}
