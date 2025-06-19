package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getNotifications(String username);
}
