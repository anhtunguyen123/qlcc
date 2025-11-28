package com.example.QuanLyCongCuBE.model;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "classrooms")
@Data
public class Classroom {
    @Id
    @Column(name = "room_id", length = 50)
    private String roomId;

    @Column(name = "room_name", nullable = false, length = 100)
    private String roomName;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "building", length = 50)
    private String building;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "has_projector")
    private Boolean hasProjector = false;

    @Column(name = "has_computer")
    private Boolean hasComputer = false;

    @Column(name = "has_sound_system")
    private Boolean hasSoundSystem = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Device> devices = new ArrayList<>();

}
