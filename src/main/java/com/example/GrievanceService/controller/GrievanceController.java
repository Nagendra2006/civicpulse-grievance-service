package com.example.GrievanceService.controller;

import com.example.GrievanceService.dto.GrievanceRequest;
import com.example.GrievanceService.dto.GrievanceResponse;
import com.example.GrievanceService.entity.Grievance;
// import com.example.GrievanceService.entity.Grievance;
import com.example.GrievanceService.service.GrievanceService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/grievances")
public class GrievanceController {

    @Autowired
    private GrievanceService grievanceService;

    @PostMapping(value = "/create", consumes = "multipart/form-data")
    public GrievanceResponse createGrievance(
            @RequestPart("data") GrievanceRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        String email = (String) httpRequest.getAttribute("email");

        System.out.println("USER ID = " + userId);
        System.out.println("EMAIL = " + email);

        return grievanceService.createGrievance(request, file, userId, email);
    }
}