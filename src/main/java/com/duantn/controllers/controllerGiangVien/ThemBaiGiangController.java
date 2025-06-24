package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThemBaiGiangController {
    @GetMapping("/tbg")
    public String showThemBaiGiang() {
        return "views/gdienGiangVien/them-bai-giang";
    }
} 