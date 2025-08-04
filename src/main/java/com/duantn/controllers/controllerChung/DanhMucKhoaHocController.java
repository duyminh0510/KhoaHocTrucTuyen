package com.duantn.controllers.controllerChung;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.DanhMucService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.DangHocService;
import com.duantn.services.CustomUserDetails;

@Controller
public class DanhMucKhoaHocController {

    @Autowired
    DanhMucService danhMucService;

    @Autowired
    KhoaHocService khoaHocService;

    @Autowired
    DangHocService dangHocService;

    @GetMapping("/{slug}")
    public String khoaHocTheoDanhMuc(@PathVariable String slug, Model model) {
        DanhMuc danhMuc = danhMucService.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        List<KhoaHoc> ds = khoaHocService.getKhoaHocTheoDanhMuc(danhMuc.getDanhmucId());

        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("khoaHocList", ds);

        // Thêm thông tin về khóa học đã mua nếu người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser") &&
                authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            TaiKhoan taiKhoan = userDetails.getTaiKhoan();
            Set<Integer> enrolledCourseIds = getEnrolledCourseIds(taiKhoan.getTaikhoanId(), ds);
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        }

        return "views/gdienChung/danhmuckhoahoc"; // tạo file HTML tương ứng
    }

    private Set<Integer> getEnrolledCourseIds(Integer taiKhoanId, List<KhoaHoc> courses) {
        return courses.stream()
                .filter(course -> dangHocService.isEnrolled(taiKhoanId, course.getKhoahocId()))
                .map(KhoaHoc::getKhoahocId)
                .collect(Collectors.toSet());
    }
}