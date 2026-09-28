package com.example.GrievanceService.workflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.GrievanceService.workflow.entity.GrievanceHistory;
import com.example.GrievanceService.workflow.repository.GrievanceHistoryRepository;

@RestController
@RequestMapping("/api/workflow/history")
public class GrievanceHistoryController {

    @Autowired
    private GrievanceHistoryRepository historyRepo;

    @GetMapping("/{id}")
    public List<GrievanceHistory> getHistory(@PathVariable Long id) {
        return historyRepo.findByGrievanceIdOrderByUpdatedAtAsc(id);
    }
}