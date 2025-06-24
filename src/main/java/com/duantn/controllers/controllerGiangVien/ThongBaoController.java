package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongBaoController {
    @GetMapping("/thong-bao")
    public String showThongBao() {
        return "views/gdienGiangVien/thong-bao";
    }
} 