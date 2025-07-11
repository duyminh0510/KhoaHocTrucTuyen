package com.duantn.controllers.controllerNhanVien;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.services.KhoaHocService;

@Controller
@RequestMapping("/")
public class DuyetKhoaHocController {

    @Autowired
    private KhoaHocService khoaHocService;

    @GetMapping("/khoahoccanduyet")
    public String hienThiDanhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocService.layTatCaKhoaHocCanDuyet();
        model.addAttribute("danhSach", danhSach);
        return "views/gdienQuanLy/duyetKhoaHoc"; // HTML path đúng
    }

    @PostMapping("/phe-duyet/{id}")
    public String pheDuyet(@PathVariable("id") Integer id) {
        Optional<KhoaHoc> optionalKhoaHoc = khoaHocService.findById(id);
        optionalKhoaHoc.ifPresent(khoaHoc -> {
            if (khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
                khoaHoc.setTrangThai(TrangThaiKhoaHoc.PUBLISHED);
                khoaHocService.save(khoaHoc);
            }
        });
        return "redirect:/khoahoccanduyet";
    }

    @PostMapping("/tu-choi/{id}")
    public String tuChoi(@PathVariable("id") Integer id) {
        Optional<KhoaHoc> optionalKhoaHoc = khoaHocService.findById(id);
        optionalKhoaHoc.ifPresent(khoaHoc -> {
            if (khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
                khoaHoc.setTrangThai(TrangThaiKhoaHoc.REJECTED);
                khoaHocService.save(khoaHoc);
            }
        });
        return "redirect:/khoahoccanduyet";
    }
}
