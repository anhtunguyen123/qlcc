package com.example.QuanLyCongCuBE.reponsitory;

import com.example.QuanLyCongCuBE.model.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

        @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
            FROM Reservation r
            WHERE r.device.deviceId = :deviceId
            AND r.status NOT IN ('CANCELLED', 'REJECTED')
            AND (r.startTime < :endTime AND r.endTime > :startTime)
            """)
        boolean existsOverlap(@Param("deviceId") String deviceId, @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

        @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END 
            FROM Reservation r 
            WHERE r.device.deviceId = :deviceId 
            AND r.status IN ('APPROVED', 'INPROGRESS', 'PENDING')
            AND r.reservationId != :excludeReservationId
            AND (
                (r.startTime < :endTime AND r.endTime > :startTime)
                OR (r.startTime = :startTime AND r.endTime = :endTime)
            )
            """)
        boolean existsOverlapExcludingCurrent(
                    @Param("deviceId") String deviceId,
                    @Param("startTime") LocalDateTime startTime,
                    @Param("endTime") LocalDateTime endTime,
                    @Param("excludeReservationId") Long excludeReservationId);

        @Query("""
            SELECT r
            FROM Reservation r
            WHERE r.device.deviceId = :deviceId
            AND CAST(r.startTime AS DATE) = :date
            ORDER BY r.startTime ASC
            """)
        List<Reservation> getScheduleByDevice(@Param("deviceId") String deviceId, @Param("date") LocalDate date);

        @Query("""
            SELECT MAX(r.endTime) FROM Reservation r
            WHERE r.device.deviceId = :deviceId
            AND r.status NOT IN ('CANCELLED', 'REJECTED')
            """)
        LocalDateTime findNextAvailableTime(@Param("deviceId") String deviceId);

        @Query("""
            SELECT r FROM Reservation r
            WHERE r.status = 'APPROVED'
            AND r.startTime BETWEEN :startTime AND :endTime
            AND NOT EXISTS (
            SELECT b FROM Borrow b WHERE b.reservation = r)
            """)
        List<Reservation> findReservationsApproved(@Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Reservation> findByUser_UserId(Long userId);

    List<Reservation> findByStatus(String status);

    @Query("""
            SELECT r FROM Reservation r
            WHERE (:status IS NULL OR r.status = :status)
            AND (:startTime IS NULL OR r.startTime >= :startTime)""")
    List<Reservation> filterReservations(
            @Param("status") String status,
            @Param("startTime") LocalDateTime startTime);
}