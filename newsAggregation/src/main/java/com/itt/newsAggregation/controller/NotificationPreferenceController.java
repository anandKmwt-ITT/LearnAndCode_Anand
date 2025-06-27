package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.NotificationPreferenceDto;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification-preferences")
public class NotificationPreferenceController {

    @Autowired
    private NotificationPreferenceService notificationPreferenceService;

    @PostMapping("/add")
    public ResponseEntity<NotificationPreferenceDto> saveNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) {
        NotificationPreferenceDto notificationPreference = notificationPreferenceService.saveNotificationPreference(notificationPreferenceDto);
        if (notificationPreference != null) {
            return ResponseEntity.ok(notificationPreference);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/update")
    public ResponseEntity<NotificationPreferenceDto> updateNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) {
        NotificationPreferenceDto updatedNotificationPreference = notificationPreferenceService.updateNotificationPreference(notificationPreferenceDto);
        if (updatedNotificationPreference != null) {
            return ResponseEntity.ok(updatedNotificationPreference);
        }
        return ResponseEntity.badRequest().build();
    }
}
