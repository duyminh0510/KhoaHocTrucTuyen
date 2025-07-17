package com.duantn.controllers.controllerChung;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.KhoaHocService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;

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
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer id = userDetails.getTaiKhoan().getTaikhoanId();
            return taiKhoanRepository.findById(id).orElse(null); // luôn lấy mới
        }
        return null;
    }

    @ModelAttribute("tenNguoiDung")
    public String getTenNguoiDung(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            TaiKhoan taiKhoanCu = ((CustomUserDetails) authentication.getPrincipal()).getTaiKhoan();
            TaiKhoan taiKhoanMoi = taiKhoanRepository.findById(taiKhoanCu.getTaikhoanId()).orElse(taiKhoanCu);
            return taiKhoanMoi.getName();
        }
        return null;
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
