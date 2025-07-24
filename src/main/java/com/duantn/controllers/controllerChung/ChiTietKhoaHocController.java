package com.duantn.controllers.controllerChung;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.Chuong;
import com.duantn.entities.DanhGia;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.ChuongService;
import com.duantn.services.DangHocService;
import com.duantn.services.DanhGiaService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TaiKhoanService;

@Controller
public class ChiTietKhoaHocController {

    @Autowired
    private DangHocService dangHocService;

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private DanhGiaService danhGiaService;
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/khoaHoc/{id}")
    public String chiTietKhoaHoc(@PathVariable("id") Integer id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        List<Chuong> chuongs = chuongService.findByKhoaHocId(id);
        long soLuongDangKy = dangHocService.demSoLuongDangKy(id);
        long soLuongDanhGia = danhGiaService.demSoLuongDanhGia(id);
        Double diemTrungBinh = danhGiaService.diemTrungBinh(id);

        // Thêm phần xử lý đánh giá
        DanhGia danhGia = new DanhGia(); // Mặc định
        if (userDetails != null) {
            TaiKhoan user = taiKhoanService.findByEmail(userDetails.getUsername());

            danhGia = danhGiaService.findByTaikhoanAndKhoahoc(user, khoaHoc)
                    .orElse(new DanhGia());
        }

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("soLuongDangKy", soLuongDangKy);
        model.addAttribute("soLuongDanhGia", soLuongDanhGia);
        model.addAttribute("diemTrungBinh", diemTrungBinh);
        model.addAttribute("danhGiaList", danhGiaService.findByKhoaHocId(id));
        model.addAttribute("danhGiaMoi", danhGia); // 👈 Dùng cho modal đánh giá

        return "views/KhoaHoc/xemChiTietKhoaHoc";
    }

}
