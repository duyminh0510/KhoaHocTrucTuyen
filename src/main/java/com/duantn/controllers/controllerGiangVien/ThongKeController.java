package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThongKeController {
    @GetMapping("/giangvien/danh-sach-thong-ke")
    public String showThemBaiGiang() {
        return "views/gdienGiangVien/thong-ke";
    }
}