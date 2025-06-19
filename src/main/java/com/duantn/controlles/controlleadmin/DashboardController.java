package com.duantn.controlles.controlleadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin")
    public String getMethodName() {
        return "views/gdienAdmin/dashboard";
    }

}
