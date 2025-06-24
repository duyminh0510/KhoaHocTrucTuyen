package com.duantn.controllers.controllerHocVien;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/hoc-vien")
public class KhoaHocDaMuaController {
    @GetMapping("/khoa-hoc-da-mua")
    public String khoaHocDaMua(Model model) {
        // Demo 2 khoá học đã mua
        List<Map<String, Object>> khoaHocDaMuaList = new ArrayList<>();
        Map<String, Object> kh1 = new HashMap<>();
        kh1.put("id", 1);
        kh1.put("tenKhoaHoc", "Lập trình Java cơ bản");
        kh1.put("moTa", "Khoá học nhập môn lập trình Java cho người mới bắt đầu.");
        kh1.put("giangVien", "Nguyen Van A");
        kh1.put("gia", new BigDecimal("1200000"));
        kh1.put("anhBia", "logochinh.png");
        kh1.put("danhGia", false); // Chưa đánh giá
        khoaHocDaMuaList.add(kh1);
        Map<String, Object> kh2 = new HashMap<>();
        kh2.put("id", 2);
        kh2.put("tenKhoaHoc", "Python cho Data Science");
        kh2.put("moTa", "Học Python từ cơ bản đến ứng dụng trong phân tích dữ liệu.");
        kh2.put("giangVien", "Le Thi B");
        kh2.put("gia", new BigDecimal("950000"));
        kh2.put("anhBia", "logochinh.png");
        kh2.put("danhGia", true); // Đã đánh giá
        khoaHocDaMuaList.add(kh2);
        model.addAttribute("khoaHocDaMuaList", khoaHocDaMuaList);
        return "views/gdienHocVien/khoa-hoc-da-mua";
    }
} 