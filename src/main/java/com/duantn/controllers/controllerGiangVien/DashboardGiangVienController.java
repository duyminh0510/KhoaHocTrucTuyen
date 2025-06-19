package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.services.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardGiangVienController {
    @Autowired
    private KhoaHocService khoaHocService;

    @GetMapping("/gv")
    public String showMyCourses(Model model) {
        List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocList", khoaHocList);
        return "views/gdienGiangVien/my-courses";
    }
}
