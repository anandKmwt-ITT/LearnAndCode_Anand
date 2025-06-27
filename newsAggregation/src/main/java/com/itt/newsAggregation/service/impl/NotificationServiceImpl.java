package com.itt.newsAggregation.service.impl;

import com.itt.newsAggregation.exception.UserNotFoundException;
import com.itt.newsAggregation.model.Notification;
import com.itt.newsAggregation.model.User;
import com.itt.newsAggregation.repository.NotificationRepository;
import com.itt.newsAggregation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public List<Notification> getNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        List<Notification> notifications = notificationRepository.findByUser(user);
        return notifications.isEmpty() ? List.of() : notifications;
    }
}
