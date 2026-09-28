package com.example.GrievanceService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.GrievanceService.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
