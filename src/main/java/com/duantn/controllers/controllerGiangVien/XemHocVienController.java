package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.DangHoc;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.DangHocRepository;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class XemHocVienController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private DangHocRepository dangHocRepository;

    @GetMapping("/giangvien/quan-ly-hoc-vien")
    public String xemDanhSachHocVien(@RequestParam(name = "khoahocId", required = false) Integer khoahocId,
            Model model,
            Principal principal) {

        TaiKhoan taiKhoan = taiKhoanRepository
                .findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        GiangVien giangVien = taiKhoan.getGiangVien();

        List<KhoaHoc> khoaHocList = khoaHocRepository.findByGiangVien(giangVien);
        model.addAttribute("khoaHocList", khoaHocList);

        List<DangHoc> dangHocList;

        if (khoahocId != null) {
            Optional<KhoaHoc> khoaHocOpt = khoaHocRepository.findById(khoahocId);
            if (khoaHocOpt.isPresent() &&
                    khoaHocOpt.get().getGiangVien().getGiangvienId().equals(giangVien.getGiangvienId())) {
                dangHocList = dangHocRepository.findByKhoahoc(khoaHocOpt.get());
                model.addAttribute("selectedKhoaHocId", khoahocId);
            } else {
                dangHocList = List.of();
            }
        } else {
            dangHocList = dangHocRepository.findByKhoahocIn(khoaHocList);
        }

        model.addAttribute("dangHocList", dangHocList);

        return "views/gdienGiangVien/quan-ly-hoc-vien";
    }

}
