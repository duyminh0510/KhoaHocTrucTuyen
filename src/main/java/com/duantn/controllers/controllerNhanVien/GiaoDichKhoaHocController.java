package com.duantn.controllers.controllerNhanVien;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class GiaoDichKhoaHocController {

    public static class GiaoDichVM {
        public Integer id;
        public String tenHocVien;
        public String email;
        public String ngayGiaoDich;
        public String hinhThuc;
        public BigDecimal tongTien;
        public String trangThai;
        public String tenKhoaHoc;
        public String maGiaoDich;
        public String ghiChu;

        public GiaoDichVM(Integer id, String tenHocVien, String email, String ngayGiaoDich, String hinhThuc,
                BigDecimal tongTien, String trangThai, String tenKhoaHoc, String maGiaoDich, String ghiChu) {
            this.id = id;
            this.tenHocVien = tenHocVien;
            this.email = email;
            this.ngayGiaoDich = ngayGiaoDich;
            this.hinhThuc = hinhThuc;
            this.tongTien = tongTien;
            this.trangThai = trangThai;
            this.tenKhoaHoc = tenKhoaHoc;
            this.maGiaoDich = maGiaoDich;
            this.ghiChu = ghiChu;
        }
    }

    private List<GiaoDichVM> listGiaoDich() {
        return Arrays.asList(
                new GiaoDichVM(1, "Nguyễn Văn A", "a@gmail.com", "01/06/2024 10:00", "VNPAY", new BigDecimal("1500000"),
                        "HOAN_THANH", "Java Cơ bản", "GDH001", "Giao dịch thành công"),
                new GiaoDichVM(2, "Trần Thị B", "b@gmail.com", "02/06/2024 14:30", "MOMO", new BigDecimal("1200000"),
                        "CHO_XU_LY", "Spring Boot Mastery", "GDH002", "Chờ xử lý"),
                new GiaoDichVM(3, "Lê Văn C", "c@gmail.com", "03/06/2024 09:15", "VNPAY", new BigDecimal("900000"),
                        "THAT_BAI", "ReactJS từ A-Z", "GDH003", "Lỗi giao dịch"));
    }

    @GetMapping("/quanly-giao-dich")
    public String listGiaoDich(Model model) {
        model.addAttribute("listGiaoDich", listGiaoDich());
        return "views/gdienQuanLy/giaoDichKhoaHoc";
    }

    @GetMapping("/quanly-giao-dich/{id}")
    public String detailGiaoDich(@PathVariable Integer id, Model model) {
        GiaoDichVM detail = null;
        for (GiaoDichVM gd : listGiaoDich()) {
            if (gd.id.equals(id)) {
                detail = gd;
                break;
            }
        }
        model.addAttribute("gd", detail);
        return "views/gdienQuanLy/giaoDichKhoaHocDetail";
    }
}