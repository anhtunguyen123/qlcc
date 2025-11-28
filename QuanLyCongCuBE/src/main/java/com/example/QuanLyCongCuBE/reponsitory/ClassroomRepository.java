package com.example.QuanLyCongCuBE.reponsitory;


import com.example.QuanLyCongCuBE.model.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends JpaRepository<Classroom, String> {

    List<Classroom> findByRoomNameContainingIgnoreCase(String roomName);

    List<Classroom> findByBuildingIgnoreCase(String building);

    List<Classroom> findDistinctByDevices_DeviceNameContainingIgnoreCase(String deviceName);
}
