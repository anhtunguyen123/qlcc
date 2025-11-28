package com.example.QuanLyCongCuBE.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyCongCuBE.service.MaintenanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class MaintenanceController {
    @Autowired
    private final MaintenanceService maintenanceService;

    @GetMapping("/count")
    public ResponseEntity<Long> countdevice(){
        
       Long count = maintenanceService.countMaintenance();
        return ResponseEntity.ok(count);
    }
}
