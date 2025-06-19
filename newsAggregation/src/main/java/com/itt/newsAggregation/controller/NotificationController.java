package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Notification>> getAllNotifications(@PathVariable String username) {
        List<Notification> notifications = notificationService.getNotifications(username);
        if (notifications.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notifications);
    }
}