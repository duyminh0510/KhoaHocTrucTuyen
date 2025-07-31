package com.duantn.controllers.controllerNhanVien;

import com.duantn.entities.RutTienGiangVien;
import com.duantn.enums.TrangThaiRutTien;
import com.duantn.repositories.RutTienGiangVienRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({ "/admin/yeu-cau-rut-tien", "/nhanvien/yeu-cau-rut-tien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class YeuCauRutTienController {
    private final RutTienGiangVienRepository rutTienRepo;

    public YeuCauRutTienController(RutTienGiangVienRepository rutTienRepo) {
        this.rutTienRepo = rutTienRepo;
    }

    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<RutTienGiangVien> danhSach = rutTienRepo.findAll();
        model.addAttribute("danhSach", danhSach);
        return "views/gdienQuanLy/yeu-cau-rut-tien";
    }

    @GetMapping("/{id}")
    public String xemChiTiet(@PathVariable Integer id, Model model) {
        RutTienGiangVien yeuCau = rutTienRepo.findById(id).orElse(null);
        if (yeuCau == null) {
            return "redirect:./"; // về danh sách
        }
        model.addAttribute("yeuCau", yeuCau);
        return "views/gdienQuanLy/chi-tiet-yeu-cau-rut-tien";
    }

    @PostMapping("/{id}/duyet")
    public String duyetYeuCauChiTiet(@PathVariable Integer id, @RequestHeader("referer") String referer) {
        RutTienGiangVien yeuCau = rutTienRepo.findById(id).orElse(null);
        if (yeuCau != null && yeuCau.getTrangthai() == TrangThaiRutTien.DANG_CHO_XU_LY) {
            yeuCau.setTrangthai(TrangThaiRutTien.THANH_CONG);
            rutTienRepo.save(yeuCau);
        }
        return "redirect:" + referer;
    }

    @PostMapping("/{id}/tuchoi")
    public String tuChoiYeuCauChiTiet(@PathVariable Integer id, @RequestHeader("referer") String referer) {
        RutTienGiangVien yeuCau = rutTienRepo.findById(id).orElse(null);
        if (yeuCau != null && yeuCau.getTrangthai() == TrangThaiRutTien.DANG_CHO_XU_LY) {
            yeuCau.setTrangthai(TrangThaiRutTien.TU_CHOI);
            rutTienRepo.save(yeuCau);
        }
        return "redirect:" + referer;
    }
}