package com.duantn.controlles.controllenhanvien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.duantn.entitys.DanhMuc;
import com.duantn.services.DanhMucService;

@Controller
@RequestMapping("/danhmuc")
public class DanhMucController {

    private final DanhMucService danhMucService;

    public DanhMucController(DanhMucService danhMucService) {
        this.danhMucService = danhMucService;
    }

    @GetMapping
    public String listDanhMuc(Model model) {
        model.addAttribute("danhmucs", danhMucService.findAll());
        model.addAttribute("danhmuc", new DanhMuc());
        return "views/gdienNhanVien/danhmuc";
    }

    @PostMapping("/add")
    public String addDanhMuc(@ModelAttribute DanhMuc danhMuc) {
        danhMucService.save(danhMuc);
        return "redirect:/danhmuc";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        DanhMuc danhMuc = danhMucService.findById(id);
        if (danhMuc == null) {
            return "redirect:/danhmuc";
        }
        model.addAttribute("danhmuc", danhMuc);
        model.addAttribute("danhmucs", danhMucService.findAll());
        return "views/gdienNhanVien/danhmuc";
    }

    @PostMapping("/edit")
    public String editDanhMuc(@ModelAttribute DanhMuc danhMuc) {
        danhMucService.save(danhMuc);
        return "redirect:/danhmuc";
    }

    @PostMapping("/delete/{id}")
    public String deleteDanhMuc(@PathVariable("id") Integer id) {
        danhMucService.deleteById(id);
        return "redirect:/danhmuc";
    }
}
