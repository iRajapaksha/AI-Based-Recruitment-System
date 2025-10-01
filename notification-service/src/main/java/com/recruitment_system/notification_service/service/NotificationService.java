package com.recruitment_system.notification_service.service;


import com.recruitment_system.event.SendVerificationEmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @KafkaListener(topics = "notification",groupId = "notificationId")
    public void sendVerificationEmail(SendVerificationEmailEvent event){
        String email = event.getEmail();
        String token = event.getToken();
        System.out.println("notfication service triggered: " +email);
        String link = String.format("http://31.97.191.209:9090/auth/verify/%s", token);
        SimpleMailMessage message = new SimpleMailMessage();
        //  message.setFrom("senderemil");
        message.setTo(email);
        message.setSubject("AI Recruitment System Registration");
        message.setText("Welcome to AI recruitment system. Click the link to verify your email: " + link);

        mailSender.send(message);


    }
}

