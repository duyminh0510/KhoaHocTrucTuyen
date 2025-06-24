package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/danh-gia")
public class DanhGiaController {

    @GetMapping("/khoa-hoc/{id}")
    public String hienThiFormDanhGia(@PathVariable("id") Long khoaHocId, Model model) {
        // Dữ liệu mẫu cho khóa học
        Map<String, Object> khoaHoc = new HashMap<>();
        khoaHoc.put("id", khoaHocId);
        khoaHoc.put("tenKhoaHoc", "Lập trình Java cơ bản");
        khoaHoc.put("giangVien", "Nguyễn Văn A");
        khoaHoc.put("anhBia", "java-course.jpg");
        khoaHoc.put("moTa", "Khóa học cung cấp kiến thức cơ bản về lập trình Java");
        
        model.addAttribute("khoaHoc", khoaHoc);
        return "views/gdienHocVien/danh-gia-khoa-hoc";
    }

    @PostMapping("/khoa-hoc/{id}")
    public String luuDanhGia(@PathVariable("id") Long khoaHocId,
                             @RequestParam("diemDanhGia") Integer diemDanhGia,
                             @RequestParam("noiDung") String noiDung,
                             Model model) {
        
        // TODO: Lưu đánh giá vào database
        // DanhGia danhGia = new DanhGia();
        // danhGia.setDiemDanhGia(diemDanhGia);
        // danhGia.setNoiDung(noiDung);
        // danhGia.setNgayDanhGia(LocalDateTime.now());
        // danhGia.setAccountId(getCurrentUserId());
        // danhGia.setCourseId(khoaHocId);
        // danhGiaService.save(danhGia);
        
        // Redirect về trang khóa học đã mua với thông báo thành công
        return "redirect:/hoc-vien/khoa-hoc-da-mua?success=true";
    }
} 