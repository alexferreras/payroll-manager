package com.payroll.service.impl;

import com.payroll.service.EmailService;
import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public boolean sendEmail(String email, String pathName) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Your paystub");
            helper.setText("Please find your paystub attached");
            File pdfFile = new File(pathName);
            if (!pdfFile.exists()) {
                return false;
            }
            helper.addAttachment("paystub.pdf", pdfFile);
            emailSender.send(message);
            return true;
        } catch (MessagingException | RuntimeException e) {
            // handle exception
            return false;
        }

    }
}