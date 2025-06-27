package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ThongKeHeThongController {

    @GetMapping("/quanly-thong-ke-he-thong")
    public String showStatisticsPage() {
        return "views/gdienQuanLy/quanly-thong-ke-he-thong";
    }

} 