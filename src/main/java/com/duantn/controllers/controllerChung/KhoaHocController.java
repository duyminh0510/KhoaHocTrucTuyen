package com.duantn.controllers.controllerChung;

import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.services.KhoaHocService;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/khoa-hoc")
@RequiredArgsConstructor
public class KhoaHocController {

    private final KhoaHocRepository khoaHocRepository;

    @Autowired
    private KhoaHocService khoaHocService;

    // ✅ Hiển thị danh sách tất cả các khóa học
    @GetMapping("")
    public String hienThiDanhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocs", danhSach);
        return "views/KhoaHo c/danhSachKhoaHoc";
    }

    // ✅ Hiển thị chi tiết khóa học (theo id), bao gồm chương và bài giảng
    @GetMapping("/{id}")
    public String getChiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocRepository.findByIdWithChaptersAndLectures(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khóa học với id = " + id));

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", khoaHoc.getChuongs()); // hoặc: chuongService.findByKhoaHocId(id)
        return "views/gdienChung/khoahocchitiet"; // Trả về đúng template
    }
}
