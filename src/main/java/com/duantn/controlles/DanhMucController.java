package com.duantn.controlles;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        model.addAttribute("danhmuc", new DanhMuc()); // form thêm mới
        return "danhmuc";
    }

    @PostMapping("/add")
    public String addDanhMuc(@ModelAttribute DanhMuc danhMuc) {
        danhMucService.save(danhMuc);
        return "redirect:/danhmuc";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        DanhMuc dm = danhMucService.findById(id);
        if (dm == null) {
            return "redirect:/danhmuc";
        }
        model.addAttribute("danhmuc", dm);
        model.addAttribute("danhmucs", danhMucService.findAll());
        return "danhmuc";
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
