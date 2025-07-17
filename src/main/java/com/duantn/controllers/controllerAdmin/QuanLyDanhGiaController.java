package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class QuanLyDanhGiaController {

    @GetMapping("/quanly-danh-gia-binh-luan")
    public String showReviewManagementPage() {
        return "views/gdienQuanLy/quanly-danh-gia";
    }

} 