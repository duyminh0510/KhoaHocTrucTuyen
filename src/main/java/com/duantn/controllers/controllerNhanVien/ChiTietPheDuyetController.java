package com.duantn.controllers.controllerNhanVien;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.ChuongService;
import com.duantn.services.KhoaHocService;

@Controller
@RequestMapping("/{prefix:(?:admin|nhanvien)}")
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class ChiTietPheDuyetController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    // ✅ Xem chi tiết khóa học
    @GetMapping("/{id}")
    public String chiTietKhoaHoc(@PathVariable("prefix") String prefix, // 👈 THÊM DÒNG NÀY
            @PathVariable("id") Integer id, Model model) {

        KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
        if (khoaHoc == null) {
            return "redirect:/duyetChiTiet/danh-sach?error=notfound";
        }

        List<Chuong> chuongs = chuongService != null ? chuongService.findByKhoaHocId(id) : null;

        // ✅ Gán prefix vào model
        model.addAttribute("prefix", prefix);
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);

        return "views/gdienQuanLy/duyetKhoaHocChiTiet";
    }
}
