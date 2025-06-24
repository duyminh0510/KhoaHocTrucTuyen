package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.services.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/hoc-vien")
public class HomeHocVienController {
    @Autowired
    private KhoaHocService khoaHocService;

    @GetMapping("/home")
    public String showHomeHocVien(Model model) {
        List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocList", khoaHocList);
        return "views/gdienHocVien/home-hoc-vien";
    }
} 