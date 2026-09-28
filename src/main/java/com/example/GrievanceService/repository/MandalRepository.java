package com.example.GrievanceService.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GrievanceService.entity.Mandal;

import java.util.List;

public interface MandalRepository extends JpaRepository<Mandal, Long> {

    // 🔥 for dropdown: /mandals?districtId=1
    List<Mandal> findByDistrictId(Long districtId);
}