package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.DanhGia;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.DanhGiaService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TaiKhoanService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("hocvien/danh-gia")
public class DanhGiaController {

    private final KhoaHocService khoaHocService;
    private final DanhGiaService danhGiaService;
    private final TaiKhoanService taiKhoanService;

    @PostMapping("/{khoahocId}")
    public String xuLyGuiDanhGia(@PathVariable("khoahocId") Integer khoahocId,
            @ModelAttribute("danhGiaMoi") DanhGia danhGia,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("Người dùng chưa đăng nhập");
        }

        TaiKhoan nguoiDung = taiKhoanService.findByEmail(userDetails.getUsername());
        KhoaHoc khoaHoc = khoaHocService.findById(khoahocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

        danhGia.setKhoahoc(khoaHoc);
        danhGiaService.taoHoacCapNhatDanhGia(khoaHoc.getKhoahocId(), nguoiDung, danhGia);

        return "redirect:/khoaHoc/" + khoahocId + "?danhgia=ok";
    }

    @GetMapping("/xoa/{khoahocId}")
    public String xoaDanhGia(@PathVariable("khoahocId") Integer khoahocId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            throw new RuntimeException("Bạn cần đăng nhập để xóa đánh giá.");
        }

        TaiKhoan nguoiDung = taiKhoanService.findByEmail(userDetails.getUsername());
        KhoaHoc khoaHoc = khoaHocService.findById(khoahocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

        danhGiaService.xoaDanhGia(khoaHoc, nguoiDung);

        return "redirect:/khoaHoc/" + khoahocId + "?danhgia=deleted";
    }
}