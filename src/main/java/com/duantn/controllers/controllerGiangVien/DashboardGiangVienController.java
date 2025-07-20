package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class DashboardGiangVienController {

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("/giangvien/trang-giang-vien")
    public String homegiangvien(Model model, Principal principal) {
        // Lấy email từ người dùng đang đăng nhập
        String email = principal.getName();

        // Tìm tài khoản theo email
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

        if (taiKhoan != null && taiKhoan.getGiangVien() != null) {
            GiangVien giangVien = taiKhoan.getGiangVien();

            // Tìm các khóa học mà giảng viên đã tạo
            List<KhoaHoc> khoaHocs = khoaHocRepository.findByGiangVien(giangVien);

            // Đưa danh sách vào model để hiển thị trong Thymeleaf
            model.addAttribute("khoaHocs", khoaHocs);
        } else {
            model.addAttribute("khoaHocs", null); // hoặc xử lý khác nếu không phải giảng viên
        }

        return "views/gdienGiangVien/home";
    }
}
