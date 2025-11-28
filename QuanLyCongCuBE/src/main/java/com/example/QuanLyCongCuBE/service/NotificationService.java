package com.example.QuanLyCongCuBE.service;

import com.example.QuanLyCongCuBE.model.Notification;
import com.example.QuanLyCongCuBE.reponsitory.NotificationRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public Notification createAndSend(Long userId, String title, String message, String relatedType, Long relatedId) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        n.setIsRead(false);
        n.setRelatedType(relatedType);
        n.setRelatedId(relatedId);

        Notification saveNotification = notificationRepository.save(n);

        // Gửi realtime đến destination /notification/{userId}
        messagingTemplate.convertAndSendToUser(userId.toString(),"/notification",n);

        return saveNotification;
    }

    public List<Notification> getByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public long countUnread(Long userId) {
        return notificationRepository.countByUserIdAndIsRead(userId, false);
    }

    public Notification markAsRead(Long notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        n.setIsRead(true);
        return notificationRepository.save(n);
    }
}
