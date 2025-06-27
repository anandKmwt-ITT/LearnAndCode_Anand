package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.NotificationPreferenceDto;

import java.util.List;

public interface NotificationPreferenceService {

    NotificationPreferenceDto saveNotificationPreference(NotificationPreferenceDto notificationPreferenceDto);
    List<NotificationPreferenceDto> getAllNotificationPreferences(String username);
    NotificationPreferenceDto updateNotificationPreference(NotificationPreferenceDto notificationPreferenceDto);
    List<NotificationPreferenceDto> getAllNotificationPreferences();
}
