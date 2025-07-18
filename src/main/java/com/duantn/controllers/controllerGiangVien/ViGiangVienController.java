package com.duantn.controllers.controllerGiangVien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duantn.entities.RutTienGiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiRutTien;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.ViGiangVienService;

@Controller
@RequestMapping("/giangvien/vi-giang-vien")
public class ViGiangVienController {

    @Autowired
    private ViGiangVienService viService;

    @GetMapping
    public String xemVi(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null || customUserDetails.getTaiKhoan() == null) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan taikhoan = customUserDetails.getTaiKhoan();
        Integer id = taikhoan.getTaikhoanId();

        model.addAttribute("taikhoan", taikhoan);
        model.addAttribute("soDu", viService.tinhSoDu(id));
        model.addAttribute("lichSuThuNhap", viService.lichSuThuNhap(id));
        model.addAttribute("lichSuRutTien", viService.lichSuRutTien(id));
        model.addAttribute("yeuCauDangXuLy", viService.yeuCauDangXuLy(id));

        // Gọi layout chính và chèn fragment content
        model.addAttribute("title", "Ví Giảng Viên");
        model.addAttribute("content", "views/gdienGiangVien/vi-giang-vien :: content");

        return "views/gdienGiangVien/vi-giang-vien"; // Đây là layout cha
    }

    @PostMapping("/rut-tien")
    public String guiYeuCauRutTien(@RequestParam("soTienRut") BigDecimal soTienRut,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null || customUserDetails.getTaiKhoan() == null) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan taikhoan = customUserDetails.getTaiKhoan();
        BigDecimal soDu = viService.tinhSoDu(taikhoan.getTaikhoanId());

        if (soTienRut.compareTo(BigDecimal.valueOf(100_000)) < 0 || soTienRut.compareTo(soDu) > 0) {
            return "redirect:/giangvien/vi-giang-vien?error=sotien";
        }

        RutTienGiangVien yeuCau = RutTienGiangVien.builder().taikhoanGV(taikhoan)
                .soTienRut(soTienRut).tenGiangVien(taikhoan.getName())
                .trangthai(TrangThaiRutTien.DAGUI_YEUCAU).ngayrut(LocalDateTime.now()).build();

        viService.luuYeuCauRutTien(yeuCau);

        return "redirect:/giangvien/vi-giang-vien?success=1";
    }
}
