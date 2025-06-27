package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hoc-vien")
public class KhoaHocChiTietController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @GetMapping("/khoa-hoc/{id}")
    public String khoaHocChiTietChung(@PathVariable("id") Integer khoaHocId, Model model, Authentication authentication) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocByIdWithDetails(khoaHocId);
        
        if (khoaHoc == null) {
            return "redirect:/hoc-vien?error=course_not_found";
        }

        model.addAttribute("khoaHoc", khoaHoc);

        if (authentication != null && authentication.isAuthenticated()) {
            taiKhoanRepository.findByEmail(authentication.getName())
                .ifPresent(taiKhoan -> {
                    model.addAttribute("tenHocVien", taiKhoan.getName());
                    Set<Integer> likedCourseIds = nguoiDungThichKhoaHocRepository
                            .findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId())
                            .stream()
                            .map(like -> like.getKhoaHoc().getKhoahocId())
                            .collect(Collectors.toSet());
                    model.addAttribute("likedCourseIds", likedCourseIds);
                });
        } else {
            model.addAttribute("likedCourseIds", Collections.emptySet());
        }
        
        return "views/gdienHocVien/khoa-hoc-chi-tiet";
    }
} 