package com.duantn.controlles.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexAdminController {

    @GetMapping("/admin/index")
    public String adminIndex() {
        return "views/Admin/AdminIndex"; // Tìm đến: templates/admin/index.html
    }
}
