package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DangNhapController {

    @GetMapping("/login")
    public String loginPage() {
        return "views/gdienChung/login";
    }

}
