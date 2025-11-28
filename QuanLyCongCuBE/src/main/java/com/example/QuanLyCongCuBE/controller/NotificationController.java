package com.example.QuanLyCongCuBE.controller;

import com.example.QuanLyCongCuBE.model.Notification;
import com.example.QuanLyCongCuBE.service.NotificationService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(notificationService.getByUser(userId));
    }

    @GetMapping("/{userId}/unreadcount")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable("userId") Long userId) {
        long count = notificationService.countUnread(userId);
        return ResponseEntity.ok(Map.of("unread", count));
    }

    @PutMapping("/mark-read/{notificationId}")
    public ResponseEntity<?> markRead(@PathVariable("notificationId") Long notificationId) {
        Notification updated = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(Map.of("status", 200, "data", updated));
    }
}
