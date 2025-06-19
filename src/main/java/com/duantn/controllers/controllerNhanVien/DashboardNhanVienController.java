package com.duantn.controllers.controllerNhanVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardNhanVienController {
    @RequestMapping("/nhanvien")
    public String requestMethodName() {
        return "views/gdienNhanVien/home";
    }
}
