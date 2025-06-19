package com.duantn.controlles.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/thongke") // Base path cho các request liên quan đến thống kê
public class ThongKeController {

    @GetMapping("")
    public String adminThongKe() {
        return "views/Admin/thongKe";
    }

}
