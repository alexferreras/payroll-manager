package com.payroll.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.payroll.service.impl.EmailServiceImpl;
import java.io.File;
import java.io.IOException;
import javax.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private MimeMessage mimeMessage;


    private EmailService emailService;

    @Before
    public void setUp() {
        emailService = new EmailServiceImpl(emailSender);

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    public void testSendEmail() throws IOException {
        String email = "example@gmail.com";
        String pathName = "example-paystub.pdf";

        File pdfFile = new File(pathName);

        if (!pdfFile.exists()) {
            pdfFile.createNewFile();
        }

        boolean result = emailService.sendEmail(email, pathName);

        pdfFile.delete();

        verify(emailSender, times(1)).send(mimeMessage);
        assertTrue(result);
    }

    @Test
    public void testSendEmailWhenIOException() {
        String email = "example@gmail.com";
        String pathName = "non-existing-file.pdf";
        boolean result = emailService.sendEmail(email, pathName);
        assertFalse(result);
    }
}