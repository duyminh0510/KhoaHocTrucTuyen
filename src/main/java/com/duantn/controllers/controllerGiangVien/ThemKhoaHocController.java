package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemKhoaHocController {
    @GetMapping("/giangvien/them-moi-khoa-hoc")
    public String showThemKhoaHoc() {
        return "views/gdienGiangVien/them-khoa-hoc";
    }
}