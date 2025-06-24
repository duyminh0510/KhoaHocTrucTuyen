package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KhoaHocCuaToiController {

    @GetMapping("/khoa-hoc-cua-toi")
    public String getMethodName() {
        return "views/gdienHocVien/khoa-hoc-cua-toi";
    }

}