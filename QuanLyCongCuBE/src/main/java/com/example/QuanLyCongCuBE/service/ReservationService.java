package com.example.QuanLyCongCuBE.service;

import java.time.LocalDateTime;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.QuanLyCongCuBE.DTO.updateReservationRequest;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.model.Reservation;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.reponsitory.DeviceReponsitory;
import com.example.QuanLyCongCuBE.reponsitory.ReservationRepository;
import com.example.QuanLyCongCuBE.reponsitory.UserReponsitory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Service
@RequiredArgsConstructor
public class ReservationService {

    @Autowired
    private final ReservationRepository reservationRepository;
    private final DeviceReponsitory deviceRepository;
    private final UserReponsitory userRepository;

    @Autowired
    private NotificationService notificationService;

    public Reservation createReservation(String deviceId, Long userId, LocalDateTime startTime, int lessonCount,
            String purpose) {

        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);

        if (!startTime.isAfter(now)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Thời gian mượn phải lớn hơn thời gian hiện tại!");
        }
        LocalDateTime endTime = startTime.plusMinutes(lessonCount * 45);

        boolean conflict = reservationRepository.existsOverlap(deviceId, startTime, endTime);

        if (conflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Thiết bị đã có người đặt trong thời gian này!");
        }

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new RuntimeException("Thiết bị không tồn tại"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Reservation reservation = new Reservation();
        reservation.setDevice(device);
        reservation.setUser(user);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setPurpose(purpose);
        reservation.setStatus("PENDING");

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationByUserId(Long UserId) {
        return reservationRepository.findByUser_UserId(UserId);
    }

    public List<Reservation> getReservation() {
        return reservationRepository.findByStatus("PENDING");
    }

    public Reservation updateStatusReser(Long reservationId, String newStatus) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu mượn"));

        LocalDateTime now = LocalDateTime.now();
        if (reservation.getStartTime().isBefore(now) && !"REJECTED".equalsIgnoreCase(newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Không thể duyệt yêu cầu đã quá hạn thời gian mượn!");
        }
        reservation.setStatus(newStatus);

        reservationRepository.save(reservation);
        Long userId = reservation.getUser().getUserId();
        String deviceName = reservation.getDevice().getDeviceName();

        if ("APPROVED".equalsIgnoreCase(newStatus)) {
            notificationService.createAndSend(userId,
                    "Yêu cầu đã được duyệt",
                    "Thiết bị " + deviceName + " đã được duyệt cho bạn.",
                    "RESERVATION",
                    reservation.getReservationId());
        }

        if ("REJECTED".equalsIgnoreCase(newStatus)) {

            Device device = deviceRepository.findById(reservation.getDevice().getDeviceId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thiết bị không tồn tại"));
            device.setStatus("AVAILABLE");
            deviceRepository.save(device);
            notificationService.createAndSend(userId,
                    "Yêu cầu bị từ chối",
                    "Yêu cầu mượn thiết bị " + deviceName + " đã bị từ chối.",
                    "RESERVATION",
                    reservation.getReservationId());
        }
        System.out.println(reservation.getStatus());
        return reservation;
    }

    public Reservation upDateReservation(Long reservationId, updateReservationRequest req) {
        System.out.println(req);
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy yêu cầu mượn với ID: " + reservationId));
        boolean isOverlap = reservationRepository.existsOverlapExcludingCurrent(
                reservation.getDevice().getDeviceId(),
                req.getStartTime(),
                req.getEndTime(),
                reservationId);

        if (isOverlap) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Thiết bị đã có lịch mượn khác trong khoảng thời gian này");
        }

        reservation.setStartTime(req.getStartTime());
        reservation.setEndTime(req.getEndTime());
        reservation.setPurpose(req.getPurpose());
        reservation.setCreatedAt(LocalDateTime.now());
        
        return reservationRepository.save(reservation);
    }

    public void deleteReservation(long id) {
        reservationRepository.deleteById(id);
    }
    

    public List<Reservation> filter(String status, LocalDate startTime) {
        LocalDateTime startDateTime = startTime != null ? startTime.atStartOfDay() : null;
        return reservationRepository.filterReservations(status, startDateTime);
    }

}