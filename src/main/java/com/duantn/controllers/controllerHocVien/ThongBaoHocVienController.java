package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThongBaoHocVienController {
    @RequestMapping("/thong-bao")
    public String requestMethodName() {
        return "views/gdienHocVien/thong-bao-hoc-vien";
    }

}
