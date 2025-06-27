package com.duantn.controllers.controllerNhanVien;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.DanhMuc;

@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class DanhMucController {

    @GetMapping("/quanly-danh-muc")
    public String hienThiDanhMuc(Model model) {
        // Mock dữ liệu để hiển thị giao diện
        List<com.duantn.entities.DanhMuc> danhSachDanhMuc = Arrays.asList(
                DanhMuc.builder().danhmucId(1).tenDanhMuc("Lập trình Java")
                        .ngayTao(LocalDateTime.now().minusDays(10)).ngayCapNhat(LocalDateTime.now())
                        .build(),
                DanhMuc.builder().danhmucId(2).tenDanhMuc("Thiết kế Web")
                        .ngayTao(LocalDateTime.now().minusDays(5)).ngayCapNhat(LocalDateTime.now())
                        .build());

        model.addAttribute("danhMuc", new DanhMuc());
        model.addAttribute("danhSachDanhMuc", danhSachDanhMuc);

        return "views/gdienQuanLy/danhmuc";
    }
}