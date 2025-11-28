package com.example.QuanLyCongCuBE.service;

import com.example.QuanLyCongCuBE.DTO.BorrowNowRequest;
import com.example.QuanLyCongCuBE.DTO.ExtendRequest;
import com.example.QuanLyCongCuBE.DTO.ReturnDevice;
import com.example.QuanLyCongCuBE.model.Borrow;
import com.example.QuanLyCongCuBE.model.Device;
import com.example.QuanLyCongCuBE.model.Maintenance;
import com.example.QuanLyCongCuBE.model.Reservation;
import com.example.QuanLyCongCuBE.model.User;
import com.example.QuanLyCongCuBE.reponsitory.BorrowRepository;
import com.example.QuanLyCongCuBE.reponsitory.DeviceReponsitory;
import com.example.QuanLyCongCuBE.reponsitory.MaintenanceRepository;
import com.example.QuanLyCongCuBE.reponsitory.ReservationRepository;
import com.example.QuanLyCongCuBE.reponsitory.UserReponsitory;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BorrowService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private DeviceReponsitory deviceRepository;

    @Autowired
    private UserReponsitory userReponsitory;

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    public Borrow createBorrowFromReservation(Long reservationId) {
        Reservation r = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy yêu cầu mượn"));

        if (!"APPROVED".equalsIgnoreCase(r.getStatus())) {
            throw new RuntimeException("Yêu cầu chưa được duyệt");
        }

        if ("BORROWED".equalsIgnoreCase(r.getDevice().getStatus())) {
            throw new RuntimeException("Thiết bị đang được mượn");
        }

        Borrow borrow = new Borrow();
        borrow.setDevice(r.getDevice());
        borrow.setUser(r.getUser());
        borrow.setReservation(r);
        borrow.setActualStartTime(LocalDateTime.now());
        borrow.setActualEndTime(r.getEndTime());
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setReturnDue(r.getEndTime());
        borrow.setPurpose(r.getPurpose());
        borrow.setStatus("BORROWED");
        borrowRepository.save(borrow);

        r.setStatus("INPROGRESS");
        reservationRepository.save(r);

        r.getDevice().setStatus("BORROWED");
        deviceRepository.save(r.getDevice());

        return borrow;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoCreateBorrowRecord() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> reservations = reservationRepository.findReservationsApproved(now.minusMinutes(1), now);

        for (Reservation r : reservations) {
            try {
                createBorrowFromReservation(r.getReservationId());
                System.out.println("Auto-created borrow for reservation" + r.getReservationId());
            } catch (Exception e) {
                System.err.println("Skip reservation" + r.getReservationId() + ": " + e.getMessage());
            }
        }
    }

    public List<Borrow> getAllBorrow() {
        return borrowRepository.findAll();
    }

    public void returnDevice(Long borrowId, ReturnDevice returnDevice) {

        System.out.println("dữ liệu be nhận" + returnDevice);

        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi mượn!"));

        if (returnDevice.getReturnDate().isBefore(borrow.getBorrowDate())) {
            throw new IllegalArgumentException("Thời gian trả không thể nhỏ hơn thời gian mượn!");
        }

        Device device = borrow.getDevice();

        device.setStatus("AVAILABLE");
        deviceRepository.save(device);

        borrow.setStatus("RETURNED");
        borrow.setReturnDate(LocalDateTime.now());

        borrowRepository.save(borrow);

        if (borrow.getReservation() != null) {
            borrow.getReservation().setStatus("COMPLETED");
        }

        if (!returnDevice.getCondition().equals("GOOD")) {
            Maintenance m = new Maintenance();
            m.setDevice(borrow.getDevice());
            m.setReportedBy(borrow.getUser());
            m.setDescription("Condition: " + returnDevice.getCondition() + ". Notes: " + returnDevice.getNotes());
            m.setReportDate(returnDevice.getReturnDate());
            m.setStatus("PENDING");
            maintenanceRepository.save(m);
        }
    }

    public Borrow ExtendBorrow(Long reservationId, ExtendRequest req) {

        Borrow borrow = borrowRepository.findByReservationReservationId(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy borrow tương ứng"));

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Không tìm thấy yêu cầu mượn"));

        if (req.getNewReturnDue().isBefore(borrow.getReturnDue())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "thoi gian tra bi loi");
        }

        boolean isOverlap = reservationRepository.existsOverlapExcludingCurrent(
                borrow.getDevice().getDeviceId(),
                borrow.getActualStartTime(),
                req.getNewReturnDue(),
                reservationId);

        if (isOverlap) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "thiet bi bi trung lich khac");
        }
        borrow.setReturnDue(req.getNewReturnDue());
        borrow.setExtensionCount(borrow.getExtensionCount() + 1);
        borrow.setNotes(req.getExtensionReason());

        reservation.setEndTime(req.getNewReturnDue());
        reservationRepository.save(reservation);
        
        return borrowRepository.save(borrow);

    }

    public Borrow createBorrowNow(BorrowNowRequest req) {
        Device device = deviceRepository.findById(req.getDeviceId())
                .orElseThrow(() -> new RuntimeException("Thiết bị không tồn tại"));

        if (!"AVAILABLE".equalsIgnoreCase(device.getStatus())) {
            throw new RuntimeException("Thiết bị hiện không sẵn sàng!");
        }

        User user = userReponsitory.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        Borrow borrow = new Borrow();
        borrow.setDevice(device);
        borrow.setUser(user);
        borrow.setActualStartTime(LocalDateTime.now());
        borrow.setActualEndTime(req.getReturnDue());
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setReturnDue(req.getReturnDue());
        borrow.setPurpose(req.getPurpose());
        borrow.setStatus("BORROWED");

        borrowRepository.save(borrow);

        device.setStatus("BORROWED");
        deviceRepository.save(device);

        return borrow;
    }

    public Long countBorrow() {
        return borrowRepository.countBorrowedDevices();
    }

    public Long totalBorrow() {
        return borrowRepository.count();
    }

    public Long countBorrowReturn() {
        return borrowRepository.countReturnBorrow();
    }

    public List<Borrow> findBorrowbyId(long userid) {
        return borrowRepository.findByUserUserIdAndStatus(userid, "BORROWED");
    }

    public List<Borrow> overDue(){
        List<Borrow> overdue = borrowRepository.findOverdueBorrows(LocalDateTime.now());
        return overdue;
    }

    public long countOverdue(){
        return borrowRepository.countOverdue(LocalDateTime.now());
    }

    public List<Borrow> getAlmostDue1h() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next1h = now.plusHours(1);
        return borrowRepository.almostDue(now, next1h);
    }

    public long countTotalBorrow(){
        return borrowRepository.count();
    }
}
   
