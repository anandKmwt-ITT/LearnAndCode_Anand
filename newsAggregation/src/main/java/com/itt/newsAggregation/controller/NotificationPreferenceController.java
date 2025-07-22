package com.itt.newsAggregation.controller;

import com.itt.newsAggregation.dto.common.NotificationPreferenceDto;
import com.itt.newsAggregation.service.NotificationPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-preferences")
public class NotificationPreferenceController {

    @Autowired
    private NotificationPreferenceService notificationPreferenceService;

    @PostMapping
    public ResponseEntity<NotificationPreferenceDto> saveNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) {
        NotificationPreferenceDto notificationPreference = notificationPreferenceService.saveNotificationPreference(notificationPreferenceDto);
        if (notificationPreference != null) {
            return ResponseEntity.ok(notificationPreference);
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping
    public ResponseEntity<NotificationPreferenceDto> updateNotificationPreference(@RequestBody NotificationPreferenceDto notificationPreferenceDto) {
        NotificationPreferenceDto updatedNotificationPreference = notificationPreferenceService.updateNotificationPreference(notificationPreferenceDto);
        if (updatedNotificationPreference != null) {
            return ResponseEntity.ok(updatedNotificationPreference);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<NotificationPreferenceDto>> getNotificationPreference(@PathVariable String username) {
        List<NotificationPreferenceDto> allNotificationPreferences = notificationPreferenceService.getAllNotificationPreferences(username);
        return new ResponseEntity<>(allNotificationPreferences, HttpStatus.OK);
    }
}
