package com.payroll.service.impl;


import static com.payroll.utils.GeneralUtil.getMailUsername;

import com.payroll.controller.request.PayrollUploadRequest;
import com.payroll.controller.response.EmailResponse;
import com.payroll.domain.PayrollData;
import com.payroll.service.EmailService;
import com.payroll.service.ImageService;
import com.payroll.service.PayrollService;
import com.payroll.service.PdfGeneratorService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class PayrollServiceImpl implements PayrollService {

    private final @NonNull PdfGeneratorService pdfGeneratorService;

    private final @NonNull EmailService emailService;

    private final @NonNull ImageService imageService;

    @Override
    public ResponseEntity uploadPayrollData(PayrollUploadRequest payrollUploadRequest) {
        try {

            String imagesPath = imageService.getImage(payrollUploadRequest.getCompany());
            CSVParser parser = parseCSV(payrollUploadRequest.getFile());
            List<PayrollData> payrollDataList = mapPayrollData(parser);
            pdfGeneratorService.generateAndSendPaystub(payrollDataList,
                payrollUploadRequest.getCountry(), imagesPath);
            List<EmailResponse> emailResponses = sendEmails(payrollDataList);
            return new ResponseEntity<>(emailResponses, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("The file can not be parse", HttpStatus.BAD_REQUEST);
        } finally {
            try {
                FileUtils.cleanDirectory(new File("src/main/resources/static/pdf/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<PayrollData> mapPayrollData(CSVParser parser) {
        try {
            return parser.getRecords()
                .stream()
                .map(this::mapPayrollData)
                .collect(Collectors.toList());
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return new ArrayList<>();
        }
    }

    private PayrollData mapPayrollData(CSVRecord record) {
        return PayrollData.builder()
            .fullName(record.get("full_name"))
            .email(record.get("email"))
            .position(record.get("position"))
            .healthDiscountAmount(Double.parseDouble(record.get("health_discount_amount")))
            .socialDiscountAmount(Double.parseDouble(record.get("social_discount_amount")))
            .taxesDiscountAmount(Double.parseDouble(record.get("taxes_discount_amount")))
            .otherDiscountAmount(Double.parseDouble(record.get("other_discount_amount")))
            .grossSalary(Double.parseDouble(record.get("gross_salary")))
            .grossPayment(Double.parseDouble(record.get("gross_payment")))
            .netPayment(Double.parseDouble(record.get("net_payment")))
            .period(record.get("period"))
            .build();
    }

    private CSVParser parseCSV(MultipartFile file) throws IOException {
        try {
            InputStream inputStream = file.getInputStream();
            return new CSVParser(new InputStreamReader(inputStream),
                CSVFormat.DEFAULT.withHeader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private List<EmailResponse> sendEmails(List<PayrollData> payrollDataList) {
        List<EmailResponse> emailResponses = new ArrayList<>();
        payrollDataList.forEach(payrollData -> {
            String email = payrollData.getEmail();
            boolean sent = emailService.sendEmail(email,
                "src/main/resources/static/pdf/" + getMailUsername(email) + "-paystubs.pdf");
            if (sent) {
                emailResponses.add(new EmailResponse(email, LocalDateTime.now()));
            }
        });

        return emailResponses;
    }
}
