package com.example.QuanLyCongCuBE.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyCongCuBE.model.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long> {
    
}
