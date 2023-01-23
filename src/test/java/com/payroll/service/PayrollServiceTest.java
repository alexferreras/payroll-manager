package com.payroll.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.payroll.domain.PayrollData;
import com.payroll.service.impl.PayrollServiceImpl;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PayrollServiceTest {
    PayrollService payrollService;
    @Mock
    ImageService imageService;
    @Mock
    PdfGeneratorService pdfGeneratorService;

    @Mock
    EmailService emailService;

    @Before
    public void setUp() {
        pdfGeneratorService = new Mockito().mock(PdfGeneratorService.class);
        emailService = new Mockito().mock(EmailService.class);
        imageService = new Mockito().mock(ImageService.class);
        payrollService = new PayrollServiceImpl(pdfGeneratorService, emailService, imageService);
    }

    @Test
    public void testParsePayrollData() throws Exception {
        String csvData =
            "johndoe,johndoe@example.com,Manager,100.00,200.00,300.00,400.00,5000.00,4800.00,4600.00,2022-01-01\n" +
                "janedoe,janedoe@example.com,Employee,50.00,100.00,150.00,200.00,3000.00,2900.00,2800.00,2022-02-01";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        CSVParser parser = new CSVParser(new InputStreamReader(inputStream),
            CSVFormat.DEFAULT.withHeader("full_name", "email", "position", "health_discount_amount",
                "social_discount_amount", "taxes_discount_amount", "other_discount_amount",
                "gross_salary", "gross_payment", "net_payment", "period"));
        List<PayrollData> payrollDataList = payrollService.mapPayrollData(parser);

        assertEquals(2, payrollDataList.size());

        PayrollData payrollData1 = payrollDataList.get(0);
        assertEquals("johndoe", payrollData1.getFullName());
        assertEquals("johndoe@example.com", payrollData1.getEmail());
        assertEquals("Manager", payrollData1.getPosition());
        assertEquals(100.00, payrollData1.getHealthDiscountAmount(), 0.001);
        assertEquals(200.00, payrollData1.getSocialDiscountAmount(), 0.001);
        assertEquals(300.00, payrollData1.getTaxesDiscountAmount(), 0.001);
        assertEquals(400.00, payrollData1.getOtherDiscountAmount(), 0.001);
        assertEquals(5000.00, payrollData1.getGrossSalary(), 0.001);
        assertEquals(4800.00, payrollData1.getGrossPayment(), 0.001);
        assertEquals(4600.00, payrollData1.getNetPayment(), 0.001);
        assertEquals("2022-01-01", payrollData1.getPeriod());

        PayrollData payrollData2 = payrollDataList.get(1);
        assertEquals("janedoe", payrollData2.getFullName());
        assertEquals("janedoe@example.com", payrollData2.getEmail());
        assertEquals("Employee", payrollData2.getPosition());
        assertEquals(50.00, payrollData2.getHealthDiscountAmount(), 0.001);
        assertEquals(100.00, payrollData2.getSocialDiscountAmount(), 0.001);
        assertEquals(150.00, payrollData2.getTaxesDiscountAmount(), 0.001);
        assertEquals(200.00, payrollData2.getOtherDiscountAmount(), 0.001);
        assertEquals(3000.00, payrollData2.getGrossSalary(), 0.001);
        assertEquals(2900.00, payrollData2.getGrossPayment(), 0.001);
        assertEquals(2800.00, payrollData2.getNetPayment(), 0.001);
        assertEquals("2022-02-01", payrollData2.getPeriod());
    }
}