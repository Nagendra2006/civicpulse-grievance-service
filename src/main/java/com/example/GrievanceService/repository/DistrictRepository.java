package com.example.GrievanceService.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GrievanceService.entity.District;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {

    // 🔥 /districts?stateId=1
    List<District> findByStateId(Long stateId);
}
