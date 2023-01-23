package com.payroll.controller;

import com.payroll.config.UserDetailConfig;
import com.payroll.controller.request.PayrollUploadRequest;
import com.payroll.service.PayrollService;
import javax.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payroll")
@RequiredArgsConstructor
public class PayrollController {
    private final @NonNull UserDetailConfig userDetailConfig;
    private final @NonNull PayrollService payrollService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity uploadPayrollData(@Valid PayrollUploadRequest payrollUploadRequest) {

        userDetailConfig.authenticate(payrollUploadRequest);

        return payrollService.uploadPayrollData(payrollUploadRequest);
    }


}