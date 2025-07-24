package com.duantn.controllers.controllerChung;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.KhoaHocService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TrangChuController {

    @Autowired
    private KhoaHocService khoaHocService;

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model, @ModelAttribute("taiKhoan") TaiKhoan taiKhoan) {
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");
        boolean isAdmin = request.isUserInRole("ROLE_ADMIN");
        boolean isNhanVien = request.isUserInRole("ROLE_NHANVIEN");

        if (isAdmin || isNhanVien) {
            return "redirect:/auth/dangnhap?error=unauthorized";
        }

        model.addAttribute("khoaHocList", khoaHocService.getTatCaKhoaHoc());
        model.addAttribute("khoaHocTheoDanhMuc", getKhoaHocTheoDanhMuc());
        return "views/gdienChung/home";
    }

    private Map<Integer, List<KhoaHoc>> getKhoaHocTheoDanhMuc() {
        Map<Integer, List<KhoaHoc>> khoaHocTheoDanhMuc = new HashMap<>();
        for (DanhMuc dm : khoaHocService.getDanhMucCoKhoaHoc()) {
            List<KhoaHoc> ds = khoaHocService.getKhoaHocTheoDanhMuc(dm.getDanhmucId());
            khoaHocTheoDanhMuc.put(dm.getDanhmucId(), ds);
        }
        return khoaHocTheoDanhMuc;
    }

}
