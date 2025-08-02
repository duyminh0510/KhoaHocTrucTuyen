package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.GiangVienService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TaiKhoanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/giangvien")
public class KhoaHocDaTaoController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private TaiKhoanService taiKhoanService; // THÊM VÀO

    @Autowired
    private GiangVienService giangVienService; // THÊM VÀO

    @GetMapping("/khoa-hoc-da-tao")
    public String khoahocdatao(@RequestParam(name = "keyword", required = false) String keyword,
            Model model,
            Principal principal) {

        TaiKhoan currentUser = taiKhoanService.findByEmail(principal.getName());
        GiangVien gv = giangVienService.findByTaiKhoan(currentUser);
        Integer giangvienId = gv.getGiangvienId();

        List<KhoaHoc> khoaHocList = khoaHocService.timTheoTenVaGiangVien(giangvienId, keyword);

        model.addAttribute("khoaHocList", khoaHocList);
        model.addAttribute("keyword", keyword);
        return "views/gdienGiangVien/khoa-hoc-da-tao";
    }
}
