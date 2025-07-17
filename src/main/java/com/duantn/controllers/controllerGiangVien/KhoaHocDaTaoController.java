package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhoaHocDaTaoController {
    @GetMapping("/giangvien/khoa-hoc-da-tao")
    public String khoahocdatao() {
        return "views/gdienGiangVien/khoa-hoc-da-tao";
    }
}

