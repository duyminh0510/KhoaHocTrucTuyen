package com.duantn.controlles.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeControllerAdmin {
    @GetMapping("/homeAdmin")
    public String home() {
        return "views/gdienAdmin/home/homeAdmin.html"; // trả về view tên home.html trong templates
    }

}
