package com.example.QuanLyCongCuBE.service;


import com.example.QuanLyCongCuBE.model.Classroom;
import com.example.QuanLyCongCuBE.reponsitory.ClassroomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClassroomService {

    @Autowired
    private final ClassroomRepository classroomRepository;


    public List<Classroom> getAllClassrooms() {
        return classroomRepository.findAll();
    }

    public Optional<Classroom> getClassroomById(String roomId) {
        return classroomRepository.findById(roomId);
    }

    public List<Classroom> searchByName(String name) {
        return classroomRepository.findByRoomNameContainingIgnoreCase(name);
    }

    public List<Classroom> searchByBuilding(String building) {
        return classroomRepository.findByBuildingIgnoreCase(building);
    }

    public List<Classroom> searchByDevice(String deviceName) {
        return classroomRepository.findDistinctByDevices_DeviceNameContainingIgnoreCase(deviceName);
    }

    public Classroom addClassroom(Classroom classroom) {
        return classroomRepository.save(classroom);
    }

    public Classroom updateClassroom(Classroom updated) {
        Optional<Classroom> cOptional = classroomRepository.findById(updated.getRoomId());

        if(cOptional.isPresent()){
            Classroom existing =  cOptional.get();
            existing.setRoomName(updated.getRoomName());
            existing.setBuilding(updated.getBuilding());
            existing.setFloor(updated.getFloor());
            existing.setCapacity(updated.getCapacity());
            existing.setHasProjector(updated.getHasProjector());
            existing.setHasComputer(updated.getHasComputer());
            existing.setHasSoundSystem(updated.getHasSoundSystem());

                return classroomRepository.save(existing);
        }else{
           throw new RuntimeException("Không tìm thấy phòng học có ID: " + updated.getRoomId());
        }  
    }

    public void deleteClassroom(String roomId) {
        classroomRepository.deleteById(roomId);
    }

    public List<Classroom> getRoom (){
        return classroomRepository.findAll();
    }
}