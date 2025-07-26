package com.duantn.controllers.controllerChung;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.KhoaHocService;
import com.duantn.services.DangHocService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String home(HttpServletRequest request, Model model, @ModelAttribute("taiKhoan") TaiKhoan taiKhoan) {
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        boolean isNhanVien = request.isUserInRole("ROLE_NHANVIEN");

        if (isAdmin || isNhanVien) {
            return "redirect:/auth/dangnhap?error=unauthorized";
        }

        model.addAttribute("khoaHocList", khoaHocService.getTatCaKhoaHoc());
        model.addAttribute("khoaHocTheoDanhMuc", getKhoaHocTheoDanhMuc());
        
        // Nếu là học viên, thêm thông tin về khóa học đã mua
        if (isHocVien && taiKhoan != null) {
            Set<Integer> enrolledCourseIds = getEnrolledCourseIds(taiKhoan.getTaikhoanId());
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);
            
            // Lấy danh sách khóa học đã mua để hiển thị trong phần "Khóa học của bạn"
            List<KhoaHoc> enrolledCourses = getEnrolledCourses(taiKhoan.getTaikhoanId());
            model.addAttribute("enrolledCourses", enrolledCourses);
        }
        
        return "views/gdienChung/home";
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
