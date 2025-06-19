package com.duantn.controllers.controllerGiangVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardGiangVienController {
    @RequestMapping("/giangvien")
    public String requestMethodName() {
        return "views/gdienGiangVien/home";
    }

}
