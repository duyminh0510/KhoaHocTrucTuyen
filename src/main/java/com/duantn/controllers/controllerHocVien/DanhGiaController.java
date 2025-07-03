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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/danh-gia")
public class DanhGiaController {

    private final KhoaHocService khoaHocService;
    private final DanhGiaService danhGiaService;
    private final TaiKhoanService taiKhoanService;

    // Hiển thị form đánh giá
    @GetMapping("/{khoahocId}")
    public String hienThiFormDanhGia(@PathVariable("khoahocId") Integer khoahocId,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        KhoaHoc khoaHoc = khoaHocService.findById(khoahocId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học"));

        model.addAttribute("khoaHoc", khoaHoc);

        DanhGia danhGia = new DanhGia();

        if (userDetails != null) {
            TaiKhoan user = taiKhoanService.findByEmail(userDetails.getUsername());
            danhGia = danhGiaService.findByTaikhoanAndKhoahoc(user, khoaHoc)
                    .orElse(new DanhGia());
        }

        model.addAttribute("danhGiaMoi", danhGia);

        return "views/gdienHocVien/danh-gia";
    }

    // Gửi đánh giá
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

        return "redirect:/khoa-hoc/" + khoahocId + "?danhgia=ok";
    }

    // Xóa đánh giá
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

        return "redirect:/khoa-hoc/" + khoahocId + "?danhgia=deleted";
    }
}
