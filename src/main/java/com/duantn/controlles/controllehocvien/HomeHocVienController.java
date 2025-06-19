package com.duantn.controlles.controllehocvien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeHocVienController {
    @RequestMapping("/hocvien")
    public String requestMethodName() {
        return "views/gdienHocVien/home";
    }
}
