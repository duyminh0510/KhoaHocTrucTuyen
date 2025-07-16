package com.duantn.controllers.controllerChung;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.DanhMucService;
import com.duantn.services.KhoaHocService;

@Controller
public class DanhMucKhoaHocController {

    @Autowired
    DanhMucService danhMucService;

    @Autowired
    KhoaHocService khoaHocService;

    @GetMapping("/{slug}")
    public String khoaHocTheoDanhMuc(@PathVariable String slug, Model model) {
        DanhMuc danhMuc = danhMucService.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));

        List<KhoaHoc> ds = khoaHocService.getKhoaHocTheoDanhMuc(danhMuc.getDanhmucId());

        model.addAttribute("danhMuc", danhMuc);
        model.addAttribute("khoaHocList", ds);

        return "views/gdienChung/danhmuckhoahoc"; // tạo file HTML tương ứng
    }
}
