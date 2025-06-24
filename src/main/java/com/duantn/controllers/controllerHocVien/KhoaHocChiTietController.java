package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.services.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/hoc-vien")
public class KhoaHocChiTietController {

    @Autowired
    private KhoaHocService khoaHocService;

    // Endpoint mới cho xem chi tiết khóa học chung (từ trang home)
    @GetMapping("/khoa-hoc/{id}")
    public String khoaHocChiTietChung(@PathVariable("id") Integer khoaHocId, Model model) {
        try {
            KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(khoaHocId);
            if (khoaHoc != null) {
                model.addAttribute("khoaHoc", khoaHoc);
                return "views/gdienHocVien/khoa-hoc-chi-tiet";
            } else {
                return "redirect:/hoc-vien?error=course_not_found";
            }
        } catch (Exception e) {
            return "redirect:/hoc-vien?error=system_error";
        }
    }

    // Endpoint cho xem chi tiết khóa học đã mua
    @GetMapping("/khoa-hoc-da-mua/{id}")
    public String khoaHocChiTiet(@PathVariable("id") Integer khoaHocId, Model model) {
        try {
            KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(khoaHocId);
            if (khoaHoc != null) {
                model.addAttribute("khoaHoc", khoaHoc);
                model.addAttribute("daMua", true); // Đánh dấu là khóa học đã mua
                return "views/gdienHocVien/khoa-hoc-chi-tiet";
            } else {
                return "redirect:/hoc-vien/khoa-hoc-da-mua?error=course_not_found";
            }
        } catch (Exception e) {
            return "redirect:/hoc-vien/khoa-hoc-da-mua?error=system_error";
        }
    }
} 