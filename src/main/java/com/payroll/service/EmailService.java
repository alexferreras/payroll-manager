package com.payroll.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    boolean sendEmail(String email, String pathName);
}
