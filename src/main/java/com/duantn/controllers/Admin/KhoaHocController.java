package com.duantn.controllers.Admin;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.duantn.entities.DanhMuc;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;

@Controller
public class KhoaHocController {

    @GetMapping("/admin/khoaHoc")
    public String hienThiKhoaHoc(Model model) {
        // Giả lập dữ liệu danh mục
        DanhMuc dm1 = DanhMuc.builder().danhmucId(1).tenDanhMuc("Lập trình").build();
        DanhMuc dm2 = DanhMuc.builder().danhmucId(2).tenDanhMuc("Thiết kế").build();

        // Giả lập giảng viên
        GiangVien gv1 = GiangVien.builder().giangvienId(1).build();
        GiangVien gv2 = GiangVien.builder().giangvienId(2).build();

        // Giả lập khóa học
        KhoaHoc kh1 = KhoaHoc.builder().khoahocId(1).tenKhoaHoc("Java Spring Boot")
                .giagoc(new BigDecimal("1000000")).giaKhuyenMai(new BigDecimal("750000"))
                .phanTramGiam(25).ngaybatdau(LocalDateTime.now())
                .ngayketthuc(LocalDateTime.now().plusMonths(1))
                .moTa("Khóa học Spring Boot cho người mới bắt đầu")
                .urlGioiThieu("https://youtube.com/demo").anhBia("springboot.jpg").trangThai(true)
                .ngayTao(LocalDateTime.now()).danhMuc(dm1).giangVien(gv1).build();

        KhoaHoc kh2 = KhoaHoc.builder().khoahocId(2).tenKhoaHoc("UI/UX Design")
                .giagoc(new BigDecimal("800000")).giaKhuyenMai(new BigDecimal("600000"))
                .phanTramGiam(25).ngaybatdau(LocalDateTime.now())
                .ngayketthuc(LocalDateTime.now().plusMonths(1))
                .moTa("Thiết kế giao diện người dùng chuyên nghiệp")
                .urlGioiThieu("https://youtube.com/uiux").anhBia("uiux.jpg").trangThai(false)
                .ngayTao(LocalDateTime.now()).danhMuc(dm2).giangVien(gv2).build();

        List<KhoaHoc> danhSachKhoaHoc = Arrays.asList(kh1, kh2);
        List<DanhMuc> dsDanhMuc = Arrays.asList(dm1, dm2);
        List<GiangVien> dsGiangVien = Arrays.asList(gv1, gv2);

        model.addAttribute("khoaHoc", new KhoaHoc());
        model.addAttribute("danhSachKhoaHoc", danhSachKhoaHoc);
        model.addAttribute("dsDanhMuc", dsDanhMuc);
        model.addAttribute("dsGiangVien", dsGiangVien);

        return "views/Admin/KhoaHoc";
    }
}
