package com.example.QuanLyCongCuBE.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.QuanLyCongCuBE.DTO.ReservationRequest;
import com.example.QuanLyCongCuBE.DTO.updateReservationRequest;
import com.example.QuanLyCongCuBE.model.Reservation;
import com.example.QuanLyCongCuBE.service.ReservationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
        try {
            Reservation reservation = reservationService.createReservation(
                    request.getDeviceId(),
                    request.getUserId(),
                    request.getStartTime(),
                    request.getLessonCount(),
                    request.getPurpose());

            return ResponseEntity.ok(reservation);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getreservation/{UserId}")
    public ResponseEntity<?> getReservation(@PathVariable("UserId") Long UserId) {
        return ResponseEntity.ok(reservationService.getReservationByUserId(UserId));
    }

    @GetMapping("/getstatuspending")
    public ResponseEntity<?> getPendingReservations() {
        List<Reservation> reservations = reservationService.getReservation();
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/updatestatus/{reservationId}")
    public ResponseEntity<?> updateReservationStatus(@PathVariable("reservationId") Long reservationId,
            @RequestBody Map<String, String> request) {

        String newStatus = request.get("status");
        Reservation updated = reservationService.updateStatusReser(reservationId, newStatus);
        return ResponseEntity.ok(Map.of(
                "status", 200,
                "message", "Cập nhật trạng thái thành công",
                "data", updated));
    }

    @DeleteMapping("/deleters/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable("reservationId") long reservationId) {
        reservationService.deleteReservation(reservationId);
        return ResponseEntity.ok(Map.of("message", "xóa thành công"));
    }

    @GetMapping("/filter")
    public List<Reservation> filterReservations(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime) {
        return reservationService.filter(status, startTime);
    }

    @PutMapping("updatereservation/{reservationId}")
    public ResponseEntity<?> updateReservation(
            @PathVariable("reservationId") Long reservationId,
            @RequestBody updateReservationRequest request) {
                
        Reservation updatedReservation = reservationService.upDateReservation(reservationId, request);
        return ResponseEntity.ok(updatedReservation);
    }

}
