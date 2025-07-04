package com.duantn.controllers.controllerGiangVien;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.services.CloudinaryService;
import com.duantn.services.DanhMucService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TaiKhoanService;

@Controller
public class ThemKhoaHocController {

    @Autowired
    DanhMucService danhMucService;

    @Autowired
    TaiKhoanService taiKhoanService;

    @Autowired
    KhoaHocService khoaHocService;

    @Autowired
    CloudinaryService cloudinaryService;

    // Hiển thị form bước 1
    @GetMapping("/giangvien/them-moi-khoa-hoc")
    public String showCourseForm(Model model) {
        model.addAttribute("course", new KhoaHoc());
        model.addAttribute("danhmuc", danhMucService.layTatCa());
        return "views/gdienGiangVien/them-khoa-hoc";
    }

    // Lưu thông tin cơ bản và chuyển qua bước 2
    @PostMapping("/giangvien/them-moi-khoa-hoc/save-info")
    public String saveBasicInfo(@ModelAttribute("course") KhoaHoc khoahoc,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("danhMuc.danhmucId") Integer danhMucId,
            Principal principal) throws IOException {

        khoahoc.setDanhMuc(danhMucService.layTheoId(danhMucId));
        khoahoc.setGiangVien(taiKhoanService.findByUsername(principal.getName()).getGiangVien());
        khoahoc.setTrangThai(TrangThaiKhoaHoc.DRAFT);

        if (file != null && !file.isEmpty()) {
            String imageUrl = cloudinaryService.uploadImage(file);
            khoahoc.setAnhBia(imageUrl);
            khoahoc.setAnhBiaPublicId(cloudinaryService.extractPublicIdFromUrl(imageUrl));
        }

        khoaHocService.save(khoahoc);

        return "redirect:/giangvien/them-moi-khoa-hoc/gia?khoahocId=" + khoahoc.getKhoahocId();
    }

    // Hiển thị form bước 2
    @GetMapping("/giangvien/them-moi-khoa-hoc/gia")
    public String showGiaForm(@RequestParam("khoahocId") Integer khoahocId, Model model) {
        KhoaHoc khoahoc = khoaHocService.getKhoaHocById(khoahocId);
        model.addAttribute("course", khoahoc);
        return "views/gdienGiangVien/them-gia-khoa-hoc";
    }

    // Lưu giá và hoàn tất
    @PostMapping("/giangvien/them-moi-khoa-hoc/save-price")
    public String savePrice(@ModelAttribute("course") KhoaHoc khoahocUpdate) {
        KhoaHoc khoahoc = khoaHocService.getKhoaHocById(khoahocUpdate.getKhoahocId());

        khoahoc.setGiagoc(khoahocUpdate.getGiagoc());
        khoahoc.setPhanTramGiam(khoahocUpdate.getPhanTramGiam());
        khoahoc.setNgaybatdau(khoahocUpdate.getNgaybatdau());
        khoahoc.setNgayketthuc(khoahocUpdate.getNgayketthuc());

        if (khoahoc.getGiagoc() != null && khoahoc.getPhanTramGiam() != null) {
            BigDecimal giam = khoahoc.getGiagoc()
                    .multiply(BigDecimal.valueOf(khoahoc.getPhanTramGiam()))
                    .divide(BigDecimal.valueOf(100));
            khoahoc.setGiaKhuyenMai(khoahoc.getGiagoc().subtract(giam));
        } else {
            khoahoc.setGiaKhuyenMai(null);
        }

        khoahoc.setShare("http://localhost:8080/khoa-hoc/" + khoahoc.getKhoahocId());
        khoaHocService.save(khoahoc);

        return "redirect:/giangvien/them-moi-khoa-hoc/them-chuong?khoahocId=" + khoahoc.getKhoahocId();
    }
}
