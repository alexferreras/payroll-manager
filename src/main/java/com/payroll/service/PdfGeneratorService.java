package com.payroll.service;

import com.itextpdf.text.pdf.PdfPCell;
import com.payroll.domain.PayrollData;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface PdfGeneratorService {
    void generateAndSendPaystub(List<PayrollData> payrollDataList, String country,
                                String imagesPath);

    PdfPCell getHeaderCell(String text, String country);
}
