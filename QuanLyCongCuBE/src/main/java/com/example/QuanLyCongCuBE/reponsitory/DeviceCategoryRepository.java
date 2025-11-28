package com.example.QuanLyCongCuBE.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.QuanLyCongCuBE.model.DeviceCategory;

public interface DeviceCategoryRepository extends JpaRepository<DeviceCategory, Integer>{
    Optional<DeviceCategory> findByCategoryName(String categoryName);
    
    List<DeviceCategory> findByCategoryNameContainingIgnoreCase(String keyword);

    void deleteBycategoryId(Long categoryId);

}

