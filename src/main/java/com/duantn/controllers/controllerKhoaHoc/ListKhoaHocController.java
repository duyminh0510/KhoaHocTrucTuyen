package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.ChuongService;
import com.duantn.services.KhoaHocService;

@Controller
public class ListKhoaHocController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    // Hiển thị danh sách tất cả khóa học
    @GetMapping("/khoaHoc")
    public String hienThiDanhSachKhoaHoc(Model model) {
        List<KhoaHoc> danhSach = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocs", danhSach);
        return "views/KhoaHoc/danhSachKhoaHoc";
    }

    // Hiển thị chi tiết khóa học, bao gồm cả danh sách chương và bài giảng
    @GetMapping("/khoaHoc/{id}")
    public String chiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        // Lấy danh sách chương theo ID khóa học
        List<Chuong> chuongs = chuongService.findByKhoaHocId(id);

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        return "views/KhoaHoc/xemChiTietKhoaHoc";
    }
}
