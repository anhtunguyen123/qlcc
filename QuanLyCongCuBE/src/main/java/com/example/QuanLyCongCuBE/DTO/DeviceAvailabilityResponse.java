package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDateTime;


import lombok.Data;

@Data
public class DeviceAvailabilityResponse {
    private String deviceId;
    private String name;
    private String location;
    private String status; 
    private String Category;
    private LocalDateTime availableAfter;
}
