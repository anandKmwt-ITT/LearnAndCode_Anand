package com.itt.newsAggregation.notification;


import com.itt.newsAggregation.dto.request.EmailRequest;

public interface NotificationSender {
    void sendNotification(EmailRequest request);
}
