package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class ExtendRequest {
    private LocalDateTime newReturnDue;
    private String extensionReason;
}
