package com.example.QuanLyCongCuBE.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class DeviceRequest {
    private String deviceId;
    private String deviceName;
    private Integer categoryId;
    private String roomId;
    private String description;
    private String specifications;
    private Boolean isPortable;
    private String status;
}