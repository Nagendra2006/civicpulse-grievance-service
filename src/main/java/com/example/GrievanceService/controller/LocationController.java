package com.example.GrievanceService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.GrievanceService.dto.DistrictResponse;
import com.example.GrievanceService.dto.MandalResponse;
import com.example.GrievanceService.dto.StateResponse;
import com.example.GrievanceService.dto.DepartmentResponse;
import com.example.GrievanceService.service.LocationService;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    // 🔹 GET STATES
    @GetMapping("/states")
    public List<StateResponse> getStates() {
        return locationService.getStates();
    }

    // 🔹 GET DISTRICTS
    @GetMapping("/districts")
    public List<DistrictResponse> getDistricts(@RequestParam Long stateId) {
        return locationService.getDistricts(stateId);
    }

    // 🔹 GET MANDALS
    @GetMapping("/mandals")
    public List<MandalResponse> getMandals(@RequestParam Long districtId) {
        return locationService.getMandals(districtId);
    }

    // 🔹 GET DEPARTMENTS
    @GetMapping("/departments")
    public List<DepartmentResponse> getDepartments() {
        return locationService.getDepartments();
    }
}
