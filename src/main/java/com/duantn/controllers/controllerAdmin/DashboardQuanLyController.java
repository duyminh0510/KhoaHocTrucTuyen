package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardQuanLyController {

    @RequestMapping({ "/admin", "/nhanvien" })
    public String home() {
        return "views/gdienQuanly/home";
    }
}
