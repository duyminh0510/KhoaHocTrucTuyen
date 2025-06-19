package com.duantn.controlles.controllegiangvien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeGiangVienController {
    @RequestMapping("/giangvien")
    public String requestMethodName() {
        return "views/gdienGiangVien/home";
    }

}
