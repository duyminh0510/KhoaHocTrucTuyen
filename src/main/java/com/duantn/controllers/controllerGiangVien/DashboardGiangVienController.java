package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.GiangVien;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.enums.TrangThaiKhoaHoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardGiangVienController {

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @GetMapping("/giangvien/trang-giang-vien")
    public String homegiangvien(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Tìm tài khoản
        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
        if (taiKhoan == null) {
            return "redirect:/dang-nhap"; // hoặc xử lý khác
        }

        // Tìm giảng viên theo tài khoản
        GiangVien giangVien = giangVienRepository.findByTaikhoan_TaikhoanId(taiKhoan.getTaikhoanId()).orElse(null);
        if (giangVien == null) {
            return "redirect:/dang-ky-giang-vien"; // hoặc xử lý khác
        }

        // Lấy danh sách khóa học của giảng viên đó (chỉ trạng thái PUBLISHED)
        List<KhoaHoc> khoaHocList = khoaHocRepository
                .findByGiangVien_GiangvienIdAndTrangThai(giangVien.getGiangvienId(), TrangThaiKhoaHoc.PUBLISHED);

        // Đưa vào model
        model.addAttribute("khoaHocList", khoaHocList);

        return "views/gdienGiangVien/home";
    }

}