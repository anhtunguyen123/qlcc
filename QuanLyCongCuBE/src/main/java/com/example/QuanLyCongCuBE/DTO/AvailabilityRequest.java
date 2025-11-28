package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AvailabilityRequest {
    private LocalDateTime startTime;
    private int lessonCount;
    private String roomId;  
    private Long categoryId;
}
