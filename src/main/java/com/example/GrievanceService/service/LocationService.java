package com.example.GrievanceService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.GrievanceService.dto.DistrictResponse;
import com.example.GrievanceService.dto.MandalResponse;
import com.example.GrievanceService.dto.StateResponse;
import com.example.GrievanceService.repository.DistrictRepository;
import com.example.GrievanceService.repository.MandalRepository;
import com.example.GrievanceService.repository.StateRepository;
import com.example.GrievanceService.repository.DepartmentRepository;
import com.example.GrievanceService.dto.DepartmentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationService {

    @Autowired
    private StateRepository stateRepo;

    @Autowired
    private DistrictRepository districtRepo;

    @Autowired
    private MandalRepository mandalRepo;

    @Autowired
    private DepartmentRepository departmentRepo;

    // 🔹 Get all states
    public List<StateResponse> getStates() {
            return stateRepo.findAll()
                            .stream()
                            .map(s -> new StateResponse(s.getId(), s.getName()))
                            .collect(Collectors.toList());
    }

    // 🔹 Get districts by state
    public List<DistrictResponse> getDistricts(Long stateId) {

        // validation
        stateRepo.findById(stateId)
                .orElseThrow(() -> new RuntimeException("State not found"));

        return districtRepo.findByStateId(stateId)
                .stream()
                .map(d -> new DistrictResponse(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }

    // 🔹 Get mandals by district
    public List<MandalResponse> getMandals(Long districtId) {

        districtRepo.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found"));

        return mandalRepo.findByDistrictId(districtId)
                .stream()
                .map(m -> new MandalResponse(m.getId(), m.getName()))
                .collect(Collectors.toList());
    }

    // 🔹 Get all departments
    public List<DepartmentResponse> getDepartments() {
        return departmentRepo.findAll()
                .stream()
                .map(d -> new DepartmentResponse(d.getId(), d.getName()))
                .collect(Collectors.toList());
    }
}
