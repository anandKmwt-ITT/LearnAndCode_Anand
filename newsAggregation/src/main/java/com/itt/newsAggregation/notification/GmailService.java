package com.itt.newsAggregation.notification;

import com.itt.newsAggregation.dto.EmailRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Slf4j
public class GmailService implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailRequest request) {
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
