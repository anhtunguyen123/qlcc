package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ReservationRequest {
    private String deviceId;
    private Long userId;
    private LocalDateTime startTime;
    private int lessonCount;
    private String purpose;
}
