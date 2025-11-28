package com.example.QuanLyCongCuBE.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.QuanLyCongCuBE.model.DeviceCategory;
import com.example.QuanLyCongCuBE.service.DeviceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DeviceCategoryController {
    @Autowired
    private final DeviceCategoryService deviceCategoryService;

    @GetMapping("/getall")
    public ResponseEntity<List<DeviceCategory>> getAllCategories() {
        List<DeviceCategory> categories = deviceCategoryService.getAllDeviceCategory();
            return ResponseEntity.ok(categories);
    }
} 


