package com.duantn.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class HoaDonPDFGenerator {

    public static byte[] taoHoaDonPDF(String tenHocVien, String giaoDichId, String tongTien,
            List<String> danhSachKhoaHoc) throws Exception {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        // Font hỗ trợ tiếng Việt
        BaseFont bf = BaseFont.createFont("src/main/resources/fonts/arial-unicode-ms.ttf", BaseFont.IDENTITY_H,
                BaseFont.EMBEDDED);
        Font fontTieuDe = new Font(bf, 18, Font.BOLD);
        Font fontChu = new Font(bf, 12);

        // Tiêu đề
        Paragraph tieuDe = new Paragraph("HÓA ĐƠN THANH TOÁN - GLOBALEDU", fontTieuDe);
        tieuDe.setAlignment(Element.ALIGN_CENTER);
        document.add(tieuDe);
        document.add(new Paragraph(" ", fontChu));

        document.add(new Paragraph("Tên học viên: " + tenHocVien, fontChu));
        document.add(new Paragraph("Mã giao dịch: " + giaoDichId, fontChu));
        document.add(new Paragraph(" ", fontChu));

        // Bảng khóa học
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[] { 3f, 1.5f });
        table.addCell(new PdfPCell(new Phrase("Khóa học", fontChu)));
        table.addCell(new PdfPCell(new Phrase("Giá (VNĐ)", fontChu)));

        for (String khoaHoc : danhSachKhoaHoc) {
            String[] parts = khoaHoc.split(";");
            table.addCell(new Phrase(parts[0], fontChu));
            table.addCell(new Phrase(parts[1] + " ₫", fontChu));
        }

        document.add(table);
        document.add(new Paragraph(" ", fontChu));
        document.add(new Paragraph("Tổng tiền: " + tongTien + " ₫", fontChu));
        document.add(new Paragraph("Cảm ơn bạn đã đăng ký học tại GLOBALEDU!", fontChu));

        document.close();
        writer.close();

        return baos.toByteArray();
    }
}
