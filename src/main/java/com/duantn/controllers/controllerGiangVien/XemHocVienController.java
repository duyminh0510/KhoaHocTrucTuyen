package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class XemHocVienController {
    @GetMapping("/giangvien/quan-ly-hoc-vien")
    public String showThemBaiTap() {
        return "views/gdienGiangVien/quan-ly-hoc-vien";
    }
}