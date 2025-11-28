package com.example.QuanLyCongCuBE.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class updateReservationRequest {
  private LocalDateTime startTime;
  private LocalDateTime  endTime;
  private String Purpose;
}
