package com.duantn.controllers.controllerChung;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.CustomOAuth2User;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.KhoaHocService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalAttributeController {

    @Autowired
    KhoaHocService khoaHocService;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @ModelAttribute("danhMucList")
    public List<DanhMuc> getDanhMucList() {
        return khoaHocService.getDanhMucCoKhoaHoc();
    }

    @ModelAttribute("taiKhoan")
    public TaiKhoan getTaiKhoan(Authentication authentication) {
        if (authentication == null) {
            System.out.println("Authentication is null");
            return null;
        }

        Object principal = authentication.getPrincipal();
        System.out.println("Principal: " + principal.getClass().getName());

        if (principal instanceof CustomUserDetails userDetails) {
            System.out.println("Logged in via username/password");
            return userDetails.getTaiKhoan();
        } else if (principal instanceof CustomOAuth2User oauth2User) {
            System.out.println("Logged in via Google OAuth");
            return oauth2User.getTaiKhoan();
        } else {
            System.out.println("Unknown principal type");
        }

        return null;
    }

    @ModelAttribute("tenNguoiDung")
    public String getTenNguoiDung(Authentication authentication) {
        TaiKhoan taiKhoan = getTaiKhoan(authentication);
        return taiKhoan != null ? taiKhoan.getName() : null;
    }

    @ModelAttribute("currentUri")
    public String getCurrentUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("likedCourseIds")
    public Set<Integer> getLikedCourseIds(Authentication authentication) {
        TaiKhoan taiKhoan = getTaiKhoan(authentication);
        if (taiKhoan != null) {
            return nguoiDungThichKhoaHocRepository
                    .findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId())
                    .stream()
                    .map(like -> like.getKhoaHoc().getKhoahocId())
                    .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }
}
