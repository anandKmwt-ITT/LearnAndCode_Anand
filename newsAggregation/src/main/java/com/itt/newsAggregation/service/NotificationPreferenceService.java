package com.itt.newsAggregation.service;

import com.itt.newsAggregation.dto.NotificationPreferenceDto;

import java.util.List;

public interface NotificationPreferenceService {

    boolean saveNotificationPreference(String username, String category, boolean isEnabled);
    List<NotificationPreferenceDto> getAllNotificationPreferences(String username);
    boolean updateNotificationPreference(String username, String category, boolean isEnabled);
    List<NotificationPreferenceDto> getAllNotificationPreferences();
}
