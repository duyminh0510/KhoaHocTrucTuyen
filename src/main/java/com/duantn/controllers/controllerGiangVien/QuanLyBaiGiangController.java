package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuanLyBaiGiangController {
    @GetMapping("/qlbg")
    public String showQuanLyBaiGiang() {
        return "views/gdienGiangVien/quan-ly-bai-giang";
    }
} 