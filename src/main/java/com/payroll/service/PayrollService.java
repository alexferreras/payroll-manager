package com.payroll.service;

import com.payroll.controller.request.PayrollUploadRequest;
import com.payroll.domain.PayrollData;
import java.util.List;
import org.apache.commons.csv.CSVParser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PayrollService {
    List<PayrollData> mapPayrollData(CSVParser parser);

    ResponseEntity uploadPayrollData(PayrollUploadRequest payrollUploadRequest);
}
