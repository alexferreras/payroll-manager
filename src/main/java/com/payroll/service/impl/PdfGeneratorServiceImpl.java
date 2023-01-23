package com.payroll.service.impl;


import static com.payroll.utils.GeneralUtil.getMailUsername;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.payroll.controller.request.Country;
import com.payroll.domain.PayrollData;
import com.payroll.service.PdfGeneratorService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGeneratorServiceImpl implements PdfGeneratorService {


    private String parseThymeleafTemplate(PayrollData payrollData, String imagesPath) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("logo", imagesPath);
        context.setVariable("payrollData", payrollData);

        return templateEngine.process("templates/paystub", context);
    }

    public void generatePdfFromHtml(String email, String html)
        throws IOException, DocumentException {
        String outputFolder =
            "src/main/resources/static/pdf/" + getMailUsername(email) + "-paystubs.pdf";
        OutputStream outputStream = new FileOutputStream(outputFolder);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);

        outputStream.close();
    }

    @Override
    public void generateAndSendPaystub(List<PayrollData> payrollDataList, String country,
                                       String imagesPath) {
        try {
            for (PayrollData payrollData : payrollDataList) {
                String html = parseThymeleafTemplate(payrollData, imagesPath);
                generatePdfFromHtml(payrollData.getEmail(), html);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public PdfPCell getHeaderCell(String text, String country) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        if (Country.USA.name().equalsIgnoreCase(country)) {
            if (text.equals("Nombre Completo")) {
                cell.setPhrase(new Phrase("Full Name"));
            } else if (text.equals("Posición")) {
                cell.setPhrase(new Phrase("Position"));
            } else if (text.equals("Salario Bruto")) {
                cell.setPhrase(new Phrase("Gross Salary"));
            } else if (text.equals("Pago Neto")) {
                cell.setPhrase(new Phrase("Net Payment"));
            }
        } else {
            cell.setPhrase(new Phrase(text));
        }
        return cell;
    }


    private PdfPTable createTable(String country) {
        // Add a table to the document
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        // Add the header row
        table.addCell(getHeaderCell("Nombre Completo", country));
        table.addCell(getHeaderCell("Posición", country));
        table.addCell(getHeaderCell("Salario Bruto", country));
        table.addCell(getHeaderCell("Pago Neto", country));
        return table;
    }

    private void addDataRowsToTable(List<PayrollData> payrollDataList, PdfPTable table) {
        // Add the data rows
        for (PayrollData payrollData : payrollDataList) {
            table.addCell(payrollData.getFullName());
            table.addCell(payrollData.getPosition());
            table.addCell(String.valueOf(payrollData.getGrossSalary()));
            table.addCell(String.valueOf(payrollData.getNetPayment()));
        }
    }


}