package com.duantn.controllers.controllerChung;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.DangHocService;
import com.duantn.services.KhoaHocService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TrangChuController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private DangHocService dangHocService;

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model, @ModelAttribute("taiKhoan") TaiKhoan taiKhoan,
            @RequestParam(defaultValue = "0") int page) {
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        boolean isNhanVien = request.isUserInRole("ROLE_NHANVIEN");

        if (isAdmin || isNhanVien) {
            return "redirect:/auth/dangnhap?error=unauthorized";
        }

        Object popupFlag = request.getSession().getAttribute("showPolicyPopup");
        if (popupFlag != null && popupFlag.equals(true)) {
            model.addAttribute("showPolicyPopup", true);
            request.getSession().removeAttribute("showPolicyPopup");
        }

        int pageSize = 12; // 12 khóa học mỗi trang
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<KhoaHoc> khoaHocPage = khoaHocService.getTatCaKhoaHocPage(pageable);

        model.addAttribute("khoaHocPage", khoaHocPage);
        model.addAttribute("khoaHocTheoDanhMuc", getKhoaHocTheoDanhMuc());

        if (isHocVien && taiKhoan != null) {
            Set<Integer> enrolledCourseIds = getEnrolledCourseIds(taiKhoan.getTaikhoanId());
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);

            // Lấy danh sách khóa học đã mua để hiển thị trong phần "Khóa học của bạn"
            List<KhoaHoc> enrolledCourses = getEnrolledCourses(taiKhoan.getTaikhoanId());
            model.addAttribute("enrolledCourses", enrolledCourses);
        }

        if (isHocVien || isGiangVien) {
            return "views/gdienHocVien/home-hocvien";
        } else {
            model.addAttribute("khoaHocList", khoaHocPage);
            return "views/gdienChung/home";
        }
    }

    private Map<Integer, List<KhoaHoc>> getKhoaHocTheoDanhMuc() {
        Map<Integer, List<KhoaHoc>> khoaHocTheoDanhMuc = new HashMap<>();
        for (DanhMuc dm : khoaHocService.getDanhMucCoKhoaHoc()) {
            List<KhoaHoc> ds = khoaHocService.getKhoaHocTheoDanhMuc(dm.getDanhmucId());
            khoaHocTheoDanhMuc.put(dm.getDanhmucId(), ds);
        }
        return khoaHocTheoDanhMuc;
    }

    private Set<Integer> getEnrolledCourseIds(Integer taiKhoanId) {
        List<KhoaHoc> allCourses = khoaHocService.getTatCaKhoaHoc();
        return allCourses.stream()
                .filter(course -> dangHocService.isEnrolled(taiKhoanId, course.getKhoahocId()))
                .map(KhoaHoc::getKhoahocId)
                .collect(Collectors.toSet());
    }

    private List<KhoaHoc> getEnrolledCourses(Integer taiKhoanId) {
        List<KhoaHoc> allCourses = khoaHocService.getTatCaKhoaHoc();
        return allCourses.stream()
                .filter(course -> dangHocService.isEnrolled(taiKhoanId, course.getKhoahocId()))
                .collect(Collectors.toList());
    }
}
