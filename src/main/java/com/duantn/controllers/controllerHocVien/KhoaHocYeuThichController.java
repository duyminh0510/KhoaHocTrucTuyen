package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhoaHocYeuThichController {

    @GetMapping("/yeu-thich")
    public String yeuthich() {
        return "views/gdienHocVien/khoa-hoc-yeu-thich";
    }
}