package com.duantn.controllers.controllerGiangVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.TaiKhoan;
import com.duantn.entities.ThongBao;
import com.duantn.repositories.ThongBaoRepository;
import com.duantn.services.CustomUserDetails;

@Controller
@RequestMapping("/giang-vien/thong-bao")
public class ThongBaoController {

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @GetMapping
    public String hienThiDanhSachThongBao(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            return "redirect:/auth/dangnhap";
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        TaiKhoan currentTaiKhoan = userDetails.getTaiKhoan();

        if (!"ROLE_GIANGVIEN".equalsIgnoreCase(currentTaiKhoan.getRole().getName())) {
            return "redirect:/access-denied";
        }

        System.out.println("ID người nhận: " + currentTaiKhoan.getTaikhoanId());

        List<ThongBao> danhSach = thongBaoRepository.findAllByNguoiNhanId(currentTaiKhoan.getTaikhoanId());
        model.addAttribute("danhSachThongBao", danhSach);

        return "views/gdienGiangVien/thong-bao";
    }
}
