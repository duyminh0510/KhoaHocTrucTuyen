package com.duantn.controllers.controllerHocVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TaiKhoanService;

import java.security.Principal;

@Controller
@RequestMapping("/hoc-vien")
public class ThanhToanController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/thanh-toan")
    public String hienThiThanhToan(@RequestParam("khoahocId") Integer id, Principal principal, Model model) {
        KhoaHoc khoaHoc = khoaHocService.findById(id).orElse(null);
        TaiKhoan taiKhoan = taiKhoanService.findByEmail(principal.getName());

        if (khoaHoc == null || taiKhoan == null) {
            return "redirect:/gio-hang";
        }

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("taiKhoan", taiKhoan);

        return "views/gDienHocVien/thanh-toan";
    }

}
