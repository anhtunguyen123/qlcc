package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BorrowNowRequest {
    private String deviceId;
    private Long userId;
    private String purpose;
    private LocalDateTime returnDue;
}
