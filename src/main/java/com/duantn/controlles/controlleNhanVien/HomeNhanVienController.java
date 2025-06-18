package com.duantn.controlles.controllenhanvien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeNhanVienController {

    @GetMapping("/nhanvien")
    public String home() {
        return "views/gdienNhanVien/home";
    }
}
