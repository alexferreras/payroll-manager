package com.payroll.service;

import static com.payroll.utils.GeneralUtil.getMailUsername;
import static org.junit.Assert.assertTrue;

import com.itextpdf.text.pdf.PdfPCell;
import com.payroll.domain.PayrollData;
import com.payroll.service.impl.PdfGeneratorServiceImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PdfGeneratorServiceTest {

    private PdfGeneratorService pdfGeneratorService = new PdfGeneratorServiceImpl();

    @Test
    public void testGeneratePaystub_defaultCountry() {
        String email = "test@gmail.com";
        List<PayrollData> payrollDataList = new ArrayList<>();
        PayrollData payrollData = PayrollData.builder().email(email).build();
        payrollDataList.add(payrollData);
        pdfGeneratorService.generateAndSendPaystub(payrollDataList, null, "img.png");
        File pdfFile = new File("src/main/resources/static/pdf/" + getMailUsername(email) + "-paystubs.pdf");
        assertTrue(pdfFile.exists());
        pdfFile.delete();
    }

    @Test
    public void testGeneratePaystub_validCountry() {
        String email = "test@gmail.com";
        List<PayrollData> payrollDataList = new ArrayList<>();
        PayrollData payrollData = PayrollData.builder().email(email).build();
        payrollDataList.add(payrollData);
        pdfGeneratorService.generateAndSendPaystub(payrollDataList, "usa","img.png");
        File pdfFile = new File("src/main/resources/static/pdf/" + getMailUsername(email) + "-paystubs.pdf");
        assertTrue(pdfFile.exists());
        pdfFile.delete();
    }

    @Test
    public void testGetHeaderCell_defaultCountry() {
        String text = "Nombre Completo";
        PdfPCell headerCell = pdfGeneratorService.getHeaderCell(text, null);
        assertTrue(headerCell.getPhrase().getContent().equals("Nombre Completo"));
    }

    @Test
    public void testGetHeaderCell_validCountry() {
        String text = "Nombre Completo";
        PdfPCell headerCell = pdfGeneratorService.getHeaderCell(text, "usa");
        assertTrue(headerCell.getPhrase().getContent().equals("Full Name"));
    }
}