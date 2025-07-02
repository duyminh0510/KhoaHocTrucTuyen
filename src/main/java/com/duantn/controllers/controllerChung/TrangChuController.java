package com.duantn.controllers.controllerChung;

import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TrangChuController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Chưa đăng nhập: chuyển tới trang chủ chung
            model.addAttribute("newCourses", khoaHocService.getNewestCourses(8));
            model.addAttribute("topCourses", khoaHocService.getTopPurchasedCourses(8));
            return "views/gdienChung/home";
        }

        // Đã đăng nhập: tùy vai trò chuyển trang
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");

        if (isHocVien) {
            List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc();
            model.addAttribute("khoaHocList", khoaHocList);

            taiKhoanRepository.findByEmail(authentication.getName()).ifPresent(taiKhoan -> {
                Set<Integer> likedCourseIds = nguoiDungThichKhoaHocRepository
                        .findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId())
                        .stream()
                        .map(like -> like.getKhoaHoc().getKhoahocId())
                        .collect(Collectors.toSet());
                model.addAttribute("likedCourseIds", likedCourseIds);
            });

            if (!model.containsAttribute("likedCourseIds")) {
                model.addAttribute("likedCourseIds", Collections.emptySet());
            }

            return "views/gdienHocVien/home";
        }

        if (isGiangVien) {
            return "views/gdienGiangVien/home";
        }

        // Nếu không phải học viên hay giảng viên
        return "redirect:/login?error=unauthorized";
    }
}
