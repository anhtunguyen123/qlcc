package com.example.QuanLyCongCuBE.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long userId;
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String message;

    private Boolean isRead = false;
    private String relatedType;
    private Long relatedId;

    private LocalDateTime createdAt = LocalDateTime.now();

}
