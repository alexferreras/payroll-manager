package com.payroll.service;


import org.springframework.stereotype.Service;

@Service
public interface ImageService {
    String getImage(String companyName);
}
