package com.duantn.controllers.controllerChung;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TrangChuController {

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {

            // Chưa đăng nhập: chuyển tới trang chủ chung
            return "views/gdienChung/home";
        }

        // Đã đăng nhập: tùy vai trò chuyển trang
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");

        if (isHocVien || isGiangVien) {
            return "views/gdienHocVien/home";
        }

        // Nếu không phải học viên hay giảng viên (phòng trường hợp khác)
        return "redirect:/login?error=unauthorized";
    }
}
