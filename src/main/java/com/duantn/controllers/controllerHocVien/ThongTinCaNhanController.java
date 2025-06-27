package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ThongTinCaNhanController {
    @RequestMapping("/tai-khoan")
    public String requestMethodName() {
        return "views/gdienHocVien/tai-khoan";
    }

}
