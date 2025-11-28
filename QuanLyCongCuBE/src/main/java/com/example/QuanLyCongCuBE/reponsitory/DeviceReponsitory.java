package com.example.QuanLyCongCuBE.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.example.QuanLyCongCuBE.model.Device;

public interface DeviceReponsitory extends JpaRepository<Device, String> {

    Optional<Device> findById(String id);

    List<Device> findByStatus(String status);

    List<Device> findByCategoryCategoryId(Integer categoryId);

    List<Device> findByClassroomRoomId(String roomId);

    @Query("""
            SELECT d FROM Device d
            WHERE (:roomId IS NULL OR d.classroom.roomId = :roomId)
            AND (:categoryId IS NULL OR d.category.categoryId = :categoryId)
            """)
    List<Device> searchDevices(
            @Param("roomId") String roomId,
            @Param("categoryId") Long categoryId);

    @Query("SELECT d FROM Device d WHERE d.status = 'AVAILABLE'")
    List<Device> findAvailableDevices();

    @Query("""
            SELECT d FROM Device d
            WHERE (:status IS NULL OR d.status = :status)
            AND (:categoryId IS NULL OR d.category.categoryId = :categoryId)
            AND (:roomId IS NULL OR d.classroom.roomId = :roomId)
            """)
    List<Device> filterDevices(
            @Param("status") String status,
            @Param("categoryId") Integer categoryId,
            @Param("roomId") String roomId);
}
