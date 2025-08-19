package com.duantn.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class CertificatePdfGenerator {
    public String generate(String studentName, String courseName, LocalDate ngayCap, String fileNameWithoutExt) throws IOException, DocumentException {
        File folder = new ClassPathResource("static/certificates").getFile();
        if (!folder.exists()) folder.mkdirs();

        String filename = fileNameWithoutExt + ".pdf";
        File file = new File(folder, filename);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 14);

        document.add(new Paragraph("CERTIFICATE OF COMPLETION", titleFont));
        document.add(new Paragraph(" ", normalFont));
        document.add(new Paragraph("Học viên: " + studentName, normalFont));
        document.add(new Paragraph("Khóa học: " + courseName, normalFont));
        document.add(new Paragraph("Ngày cấp: " + ngayCap.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), normalFont));
        document.add(new Paragraph("\nChúc mừng bạn đã hoàn thành khóa học!", normalFont));
        document.close();

        return "/certificates/" + filename;
    }
}
