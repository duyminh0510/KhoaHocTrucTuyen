package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;

@Controller
@RequestMapping("/duyetKhoaHoc")
public class pheDuyetKhoaHocController {

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @GetMapping("/danh-sach")
    public String danhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocRepository.findAll();
        model.addAttribute("danhSach", danhSach);
        return "views/KhoaHoc/pheDuyetKhoaHoc";
    }

    @PostMapping("/phe-duyet/{id}")
    public String pheDuyet(@PathVariable("id") Integer id) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id).orElse(null);
        if (khoaHoc != null && khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
            khoaHoc.setTrangThai(TrangThaiKhoaHoc.PUBLISHED);
            khoaHocRepository.save(khoaHoc);
        }
        return "redirect:/duyetKhoaHoc/danh-sach";
    }

    @PostMapping("/tu-choi/{id}")
    public String tuChoi(@PathVariable("id") Integer id) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id).orElse(null);
        if (khoaHoc != null && khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
            khoaHoc.setTrangThai(TrangThaiKhoaHoc.REJECTED);
            khoaHocRepository.save(khoaHoc);
        }
        return "redirect:/duyetKhoaHoc/danh-sach";
    }
}
