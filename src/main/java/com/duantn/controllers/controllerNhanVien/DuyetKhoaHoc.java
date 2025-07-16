package com.duantn.controllers.controllerNhanVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.KhoaHoc;
import com.duantn.services.KhoaHocService;


@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class DuyetKhoaHoc {
    @Autowired
    private KhoaHocService khoaHocService;

    @GetMapping("/khoahoccanduyet")
    public String hienThiDanhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocService.layTatCaKhoaHocCanDuyet();
        model.addAttribute("dsKhoaHoc", danhSach);
        return "views/gdienQuanLy/khoaHocCanDuyet";
    }
}
