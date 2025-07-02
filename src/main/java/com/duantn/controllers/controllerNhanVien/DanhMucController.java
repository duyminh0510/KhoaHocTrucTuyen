package com.duantn.controllers.controllerNhanVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.DanhMuc;
import com.duantn.services.DanhMucService;

@Controller
@RequestMapping("/quanly-danh-muc")
public class DanhMucController {

    @Autowired
    private DanhMucService service;

    @GetMapping
    public String trangDanhSach(Model model) {
        List<DanhMuc> danhmucs = service.findAll();
        model.addAttribute("danhmucs", danhmucs);
        model.addAttribute("danhmuc", new DanhMuc());
        return "views/gdienQuanLy/danhmuc";
    }

    @PostMapping("/them-moi")
    public String them(@ModelAttribute("danhmuc") DanhMuc danhMuc, Model model) {
        if (service.daTonTaiTen(danhMuc.getTenDanhMuc())) {
            model.addAttribute("error", "Tên danh mục đã tồn tại!");
            model.addAttribute("danhmucs", service.findAll());
            return "views/gdienQuanLy/danhmuc";
        }

        service.save(danhMuc);
        return "redirect:/quanly-danh-muc";
    }

    @GetMapping("/edit/{id}")
    public String suaForm(@PathVariable Integer id, Model model) {
        DanhMuc dm = service.findById(id);
        model.addAttribute("danhmucs", service.findAll());
        model.addAttribute("danhmuc", dm);
        return "views/gdienQuanLy/danhmuc";
    }

    @PostMapping("/edit")
    public String capNhat(@ModelAttribute("danhmuc") DanhMuc danhMuc, Model model) {
        if (service.daTonTaiTenKhacId(danhMuc.getTenDanhMuc(), danhMuc.getDanhmucId())) {
            model.addAttribute("error", "Tên danh mục đã tồn tại!");
            model.addAttribute("danhmucs", service.findAll());
            model.addAttribute("danhmuc", danhMuc);
            return "views/gdienQuanLy/danhmuc";
        }

        service.save(danhMuc);
        return "redirect:/quanly-danh-muc";
    }

    @GetMapping("/vo-hieu-hoa/{id}")
    public String xoa(@PathVariable Integer id) {
        service.voHieuHoaDanhMuc(id);
        return "redirect:/quanly-danh-muc";
    }

    @GetMapping("/kich-hoat/{id}")
    public String kichHoat(@PathVariable Integer id) {
        service.kichHoatDanhMuc(id);
        return "redirect:/quanly-danh-muc";
    }
}