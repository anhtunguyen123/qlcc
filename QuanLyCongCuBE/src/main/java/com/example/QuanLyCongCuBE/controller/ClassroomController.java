package com.example.QuanLyCongCuBE.controller;

import com.example.QuanLyCongCuBE.model.Classroom;
import com.example.QuanLyCongCuBE.service.ClassroomService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classrooms")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClassroomController {

    private final ClassroomService classroomService;

    @GetMapping
    public ResponseEntity<List<Classroom>> getAllClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Classroom> getClassroomById(@PathVariable("id") String id) {
        return  classroomService.getClassroomById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/name")
    public ResponseEntity<List<Classroom>> searchByName(@RequestParam("query") String name) {
        return ResponseEntity.ok(classroomService.searchByName(name));
    }

    @GetMapping("/search/device")
    public ResponseEntity<List<Classroom>> searchByDevice(@RequestParam("query") String deviceName) {
        return ResponseEntity.ok(classroomService.searchByDevice(deviceName));
    }

    @PostMapping("/create")
    public ResponseEntity<Classroom> addClassroom(@RequestBody Classroom classroom) {
        return ResponseEntity.ok(classroomService.addClassroom(classroom));
    }

    @PutMapping("/update")
    public ResponseEntity<Classroom> updateClassroom(@RequestBody Classroom classroom) {
        return ResponseEntity.ok(classroomService.updateClassroom(classroom));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClassroom(@PathVariable("id") String id) {
        classroomService.deleteClassroom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getallroom")
    public ResponseEntity<List<Classroom>> getAllRooms() {
        List<Classroom> listroom = classroomService.getRoom();
        return ResponseEntity.ok(listroom);
    }
}