package com.example.GrievanceService.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GrievanceService.entity.State;

public interface StateRepository extends JpaRepository<State, Long> {
}
