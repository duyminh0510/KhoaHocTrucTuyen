package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardAdminController {

    @GetMapping("/admin")
    public String getMethodName() {
        return "views/gdienAdmin/dashboard";
    }

}
