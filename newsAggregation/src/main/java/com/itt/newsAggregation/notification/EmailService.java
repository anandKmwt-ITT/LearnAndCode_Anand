package com.itt.newsAggregation.notification;


import com.itt.newsAggregation.dto.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest request);
}
