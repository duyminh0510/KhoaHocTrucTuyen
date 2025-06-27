package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViGiangVienController {
    @RequestMapping("/giangvien/vi-giang-vien")
    public String requestMethodName() {
        return "views/gdienGiangVien/vi-giang-vien";
    }

}
