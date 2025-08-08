package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class DangNhapController {

    @GetMapping("/dangnhap")
    public String loginPage() {
        return "views/gdienChung/login";
    }

}
