package com.example.QuanLyCongCuBE.controller;

import com.example.QuanLyCongCuBE.DTO.BorrowNowRequest;
import com.example.QuanLyCongCuBE.DTO.ExtendRequest;
import com.example.QuanLyCongCuBE.DTO.ReturnDevice;
import com.example.QuanLyCongCuBE.model.Borrow;
import com.example.QuanLyCongCuBE.service.BorrowService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class BorrowController {
    @Autowired
    final private BorrowService borrowService;

    @PostMapping("/create/{reservationId}")
    public ResponseEntity<?> borrowEarly(@PathVariable Long reservationId) {
        try {
            Borrow borrow = borrowService.createBorrowFromReservation(reservationId);
            return ResponseEntity.ok(borrow);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getborrow")
    public ResponseEntity<?> getBorrow() {
        try {
            List<Borrow> listBorrow = borrowService.getAllBorrow();
            return ResponseEntity.ok(listBorrow);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/createnow")
    public ResponseEntity<?> createBorrowNow(@RequestBody BorrowNowRequest req) {
        Borrow borrow = borrowService.createBorrowNow(req);
        return ResponseEntity.ok(borrow);
    }

    @GetMapping("/countborrow")
    public Long countBorrow() {
        return borrowService.countBorrow();
    }

    @PostMapping("/returnborrow/{borrowId}")
    public void returnBorrow(@PathVariable("borrowId") Long borrowId, @RequestBody ReturnDevice returnDevice) {
        borrowService.returnDevice(borrowId, returnDevice);
    }

    @GetMapping("/getborrowbyid/{userId}")
    public List<Borrow> getBorrowById(@PathVariable("userId") Long userId) {
        return borrowService.findBorrowbyId(userId);
    }

    @PutMapping("/extend/{reservationId}")
    public Borrow extendBorrow(
            @PathVariable("reservationId") Long reservationId,
            @RequestBody ExtendRequest request) {

                System.out.println("Vào controller ExtendBorrow, reservationId=" + reservationId);
                Borrow result = borrowService.ExtendBorrow(reservationId, request);
                System.out.println("Kết quả save borrow: " + result);
                return result;
    }

    @GetMapping("/getborrowover")
    public List<Borrow> getlistBorrowOver() {
        return borrowService.overDue();
    }

    @GetMapping("/countoverdue")
    public long getOverdueCount() {
        return borrowService.countOverdue();
    }

    @GetMapping("/almostDue")
    public List<Borrow> getAlmostDue(){
        return borrowService.getAlmostDue1h();
    }

    @GetMapping("/counttotalborrow")
    public Long countTotalBorrow(){
        return borrowService.totalBorrow();
    }

    @GetMapping("/countborrowreturn")
    public Long countReturnBorrow(){
        return borrowService.countBorrowReturn();
    }
}
