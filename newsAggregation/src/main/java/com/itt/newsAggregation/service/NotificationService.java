package com.itt.newsAggregation.service;

import com.itt.newsAggregation.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(String username);
    void saveNotification(Notification notification);
}
