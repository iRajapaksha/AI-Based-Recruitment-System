package com.recruitment_system.notification_service.service;

import com.recruitment_system.event.ConfirmationEmailEvent;
import com.recruitment_system.event.SendVerificationEmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @KafkaListener(
            topics = "notification",
            groupId = "notificationId",
            containerFactory = "verificationKafkaListenerFactory"
    )
    public void sendVerificationEmail(SendVerificationEmailEvent sendVerificationEmailEvent){
        String email = sendVerificationEmailEvent.getEmail();
        String token = sendVerificationEmailEvent.getToken();
        log.info("notification service triggered for verification email to {}", email);
        String link = String.format("http://31.97.191.209:9090/auth/verify/%s", token);

        String subject = "AI Recruitment System Registration";
        String body = "Welcome to AI recruitment system. Click the link to verify your email: " + link;

        try {
            sendEmail(email, subject, body);
            log.info("Verification email sent to {}", email);
        } catch (Exception ex) {
            log.error("Failed to send verification email to {} : {}", email, ex.getMessage(), ex);
            // optionally rethrow or perform retry logic
            throw new RuntimeException(ex);
        }
    }

    @KafkaListener(
            topics = "confirmation-email",
            groupId = "notification-group",
            containerFactory = "confirmationKafkaListenerFactory"
    )
    public void handleConfirmationEmail(ConfirmationEmailEvent confirmationEmailEvent) {
        try {
            log.info("Received email event - subject:{}  to:{}",
                    confirmationEmailEvent.getSubject(),
                    confirmationEmailEvent.getEmail());

            // Send the email using helper
            sendEmail(confirmationEmailEvent.getEmail(),
                    confirmationEmailEvent.getSubject(),
                    confirmationEmailEvent.getBody());

            log.info("Confirmation email successfully sent to {}", confirmationEmailEvent.getEmail());

        } catch (Exception ex) {
            log.error("Failed to send email to {}: {}",
                    confirmationEmailEvent.getEmail(),
                    ex.getMessage(),
                    ex);
            throw new RuntimeException(ex);
        }
    }

}
