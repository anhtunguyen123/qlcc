package com.example.QuanLyCongCuBE.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.QuanLyCongCuBE.reponsitory.MaintenanceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MaintenanceService {
    @Autowired 
    private final MaintenanceRepository maintenanceRepository;

    public Long countMaintenance() {
        return maintenanceRepository.count();
    }
}
