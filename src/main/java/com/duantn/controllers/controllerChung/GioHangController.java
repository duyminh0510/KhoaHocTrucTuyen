package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {
    @RequestMapping()
    public String requestMethodName() {
        return "views/gdienChung/giohang";
    }

}
