package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardGiangVienController {

    @GetMapping("/giangvien/trang-giang-vien")
    public String homegiangvien() {
        return "views/gdienGiangVien/home";
    }

}
