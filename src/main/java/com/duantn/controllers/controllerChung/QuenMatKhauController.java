package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lay-lai-mat-khau")
public class QuenMatKhauController {
    @RequestMapping()
    public String requestMethodName() {
        return "views/gdienChung/quenmatkhau";
    }

}
