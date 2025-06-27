package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class QuanLyNhanVienController {

    @GetMapping("/quanly-nhanvien")
    public String showEmployeeManagementPage() {
        return "views/gdienQuanLy/quanly-nhanvien";
    }
} 