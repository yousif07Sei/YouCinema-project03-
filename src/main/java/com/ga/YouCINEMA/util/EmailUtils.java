package com.ga.YouCINEMA.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EmailUtils {

    private final JavaMailSender mailSender;


    public void sendVerificationEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("YouCinema - Verify Your Email");
        message.setText("Hi! \n\nPlease verify your email by clicking the link below:\n\n"
                + "http://localhost:8080/auth/verify-email?token=" + token
                + "\n\nThis link expires in 24 hours."
                + "\n\nYouCinema Team");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String toEmail, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("YouCinema - Reset Your Password");
        message.setText("Hi! \n\nYou requested to reset your password.\n\n"
                + "Your reset token is: " + token
                + "\n\nUse this token in the reset password endpoint."
                + "\n\nThis token expires in 1 hour."
                + "\n\nIf you did not request this, please ignore this email."
                + "\n\nYouCinema Team");
        mailSender.send(message);
    }
}