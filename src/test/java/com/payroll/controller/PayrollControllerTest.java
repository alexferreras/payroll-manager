package com.payroll.controller;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.payroll.config.UserDetailConfig;
import com.payroll.controller.request.PayrollUploadRequest;
import com.payroll.controller.response.EmailResponse;
import com.payroll.domain.PayrollData;
import com.payroll.service.PayrollService;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class PayrollControllerTest {
    @Mock
    PayrollService payrollService;
    @Mock
    UserDetailConfig userDetailConfig;


    MultipartFile file;

    PayrollController payrollController;

    String country;
    List<PayrollData> payrollDataList;
    List<EmailResponse> emailResponses;
    File file1;
    HttpHeaders headers;

    PayrollData payrollData;

    @Before
    public void setUp() {
        country = "USA";
        payrollDataList = new ArrayList<>();

        emailResponses = new ArrayList<>();
        emailResponses.add(new EmailResponse("test@test.com", LocalDateTime.now()));
        userDetailConfig = new UserDetailConfig("user", "pwd");
        file1 = new File("paystub.pdf");
        headers = new HttpHeaders();
        payrollData = PayrollData.builder().email("test@test.com").build();
        payrollDataList.add(payrollData);
        file = new MockMultipartFile("file", "payrollData.csv", "text/csv", "".getBytes());

        payrollController = new PayrollController(userDetailConfig,payrollService);

    }


    @Test
    public void testUploadPayrollData_success() {
        when(payrollService.uploadPayrollData(any(PayrollUploadRequest.class)))
            .thenReturn(new ResponseEntity<>(emailResponses, HttpStatus.OK));
        var request = PayrollUploadRequest.builder()
            .file(file).company("USA").credentials("user+pwd").company("company").build();
        request.isDataFullFill();
        ResponseEntity response = payrollController.uploadPayrollData(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

}