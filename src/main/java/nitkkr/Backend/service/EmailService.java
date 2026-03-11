package nitkkr.Backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {
        try {
            System.out.println("=== BREVO EMAIL SERVICE DEBUG ===");
            System.out.println("Sending OTP to: " + toEmail);
            System.out.println("OTP: " + otp);
            System.out.println("==============================");

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("raosabh1981@gmail.com");
            message.setTo(toEmail);
            message.setSubject("NIT KKR Lost & Found - OTP Verification");
            message.setText("Your OTP is: " + otp + "\n\nThis OTP is valid for 5 minutes.\n\nIf you didn't request this, please ignore this email.\n\n 2024 NIT Kurukshetra Lost & Found Portal");

            mailSender.send(message);
            
            System.out.println(" Email sent successfully via Brevo to: " + toEmail);
        } catch (Exception e) {
            System.err.println(" BREVO EMAIL ERROR: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
}