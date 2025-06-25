package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.KhoaHocRepository;

@Controller
public class ListKhoaHocController {

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @GetMapping("/khoaHoc")
    public String hienThiDanhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocRepository.findAll();
        model.addAttribute("khoaHocs", danhSach);
        return "views/KhoaHoc/danhSachKhoaHoc";
    }

    @GetMapping("/khoaHoc/{id}")
    public String chiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id: " + id));
        model.addAttribute("khoaHoc", khoaHoc);
        return "views/KhoaHoc/xemChiTietKhoaHoc";
    }
}
