package com.duantn.controllers.controllerGiangVien;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.GiangVienService;
import com.duantn.services.ViGiangVienService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/giangvien/vi-giang-vien")
public class ViGiangVienController {
    @Autowired
    private ViGiangVienService viGiangVienService;

    @Autowired
    private GiangVienService giangVienService;  

    @GetMapping
    public String hienThiVi(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaiKhoan giangVien = userDetails.getTaiKhoan();

        model.addAttribute("soDu", viGiangVienService.tinhSoDu(giangVien));
        model.addAttribute("lichSuThuNhap", viGiangVienService.getLichSuThuNhap(giangVien));
        model.addAttribute("lichSuRutTien", viGiangVienService.getLichSuRutTienThanhCong(giangVien));
        model.addAttribute("yeuCauDangXuLy", viGiangVienService.getYeuCauDangXuLy(giangVien));

        return "views/gdienGiangVien/vi-giang-vien";
    }

    @PostMapping("/rut-tien")
    public String guiYeuCauRutTien(@RequestParam("soTienRut") BigDecimal soTienRut) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        TaiKhoan giangVien = userDetails.getTaiKhoan();

        if (viGiangVienService.guiYeuCauRutTien(giangVien, soTienRut)) {
            return "redirect:/giangvien/vi-giang-vien?success";
        }
        return "redirect:/giangvien/vi-giang-vien?error";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getThongTin(@PathVariable("id") Integer id) {
        GiangVien gv = giangVienService.getById(id);
        if (gv != null) {
            return ResponseEntity.ok(gv); // Spring boot sẽ tự chuyển thành JSON
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cap-nhat-ngan-hang")
    public void capNhatThongTinNganHang(
            @RequestParam("giangVienId") Integer giangVienId,
            @RequestParam("soTaiKhoan") String soTaiKhoan,
            @RequestParam("tenNganHang") String tenNganHang,
            HttpServletResponse response
    ) {
        boolean success = giangVienService.capNhatThongTinNganHang(giangVienId, soTaiKhoan, tenNganHang);
        if (success) {
            response.setStatus(HttpServletResponse.SC_OK); // 200
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
        }
    }
}
