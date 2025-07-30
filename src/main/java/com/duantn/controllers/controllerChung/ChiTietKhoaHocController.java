package com.duantn.controllers.controllerChung;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.ChuongService;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.DangHocService;
import com.duantn.services.DanhGiaService;
import com.duantn.services.KhoaHocService;

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

    @GetMapping("/khoaHoc/{id}")
    public String chiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        List<Chuong> chuongs = chuongService.findByKhoaHocId(id);

        long soLuongDangKy = dangHocService.demSoLuongDangKy(id);
        long soLuongDanhGia = danhGiaService.demSoLuongDanhGia(id);
        Double diemTrungBinh = danhGiaService.diemTrungBinh(id);

        model.addAttribute("danhGiaList", danhGiaService.findByKhoaHocId(id));
        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("soLuongDangKy", soLuongDangKy);
        model.addAttribute("soLuongDanhGia", soLuongDanhGia);
        model.addAttribute("diemTrungBinh", diemTrungBinh);

        // Thêm thông tin về khóa học đã mua nếu người dùng đã đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !authentication.getName().equals("anonymousUser") &&
                authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            TaiKhoan taiKhoan = userDetails.getTaiKhoan();
            boolean isEnrolled = dangHocService.isEnrolled(taiKhoan.getTaikhoanId(), id);
            model.addAttribute("isEnrolled", isEnrolled);
        }

        return "views/KhoaHoc/xemChiTietKhoaHoc";
    }

}