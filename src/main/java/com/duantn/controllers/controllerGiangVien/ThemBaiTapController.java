package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemBaiTapController {
    @GetMapping("/tbt")
    public String showThemBaiTap() {
        return "views/gdienGiangVien/them-bai-tap";
    }
} 