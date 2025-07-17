package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.services.ThongKeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin")
public class ThongKeHeThongController {

    @Autowired
    private ThongKeService thongKeService;

    @GetMapping("/quanly-thong-ke-he-thong")
    public String showStatisticsPage(Model model) {
        model.addAttribute("tongHocVien", thongKeService.tongHocVien());
        model.addAttribute("tongGiangVien", thongKeService.tongGiangVien());
        model.addAttribute("tongKhoaHoc", thongKeService.tongKhoaHoc());
        model.addAttribute("doanhThuThang", thongKeService.doanhThuThangNay());
        model.addAttribute("revenueData", thongKeService.getDoanhThu6Thang());
        model.addAttribute("categoryData", thongKeService.getTiLeDanhMuc());
        model.addAttribute("chiTietKhoaHoc", thongKeService.getChiTietKhoaHoc());
        return "views/gdienQuanLy/quanly-thong-ke-he-thong";
    }

}