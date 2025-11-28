package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReturnDevice {
    private Long borrowId;
    private LocalDateTime returnDate;
    private String status;
    private String purpose;
    private String condition;
    private String notes;
    private String receivedBy;
}
