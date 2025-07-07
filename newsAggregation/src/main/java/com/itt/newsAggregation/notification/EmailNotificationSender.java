package com.itt.newsAggregation.notification;

import com.itt.newsAggregation.dto.request.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationSender implements NotificationSender {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    @Async
    public void sendNotification(EmailRequest request) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(request.getTo());
            mail.setSubject(request.getSubject());
            mail.setText(request.getBody());
            javaMailSender.send(mail);
        } catch (Exception e) {
            log.error("Exception while sendEmail ", e);
        }
    }

}
