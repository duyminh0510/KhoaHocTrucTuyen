package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GioiThieuController {
    @RequestMapping("/gioithieu")
    public String requestMethodName() {
        return "views/gdienChung/gioithieu";
    }

}
