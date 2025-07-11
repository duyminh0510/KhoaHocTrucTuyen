package com.duantn.controllers.controllerNhanVien;

import com.duantn.entities.GiaoDichKhoaHoc;
import com.duantn.services.GiaoDichService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;

import java.util.List;

@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class GiaoDichKhoaHocController {

    private final GiaoDichService giaoDichService;

    public GiaoDichKhoaHocController(GiaoDichService giaoDichService) {
        this.giaoDichService = giaoDichService;
    }

    // @GetMapping("/quanly-giao-dich")
    // public String listGiaoDich(Model model) {
    //     List<GiaoDichKhoaHoc> listGiaoDich = giaoDichService.getAllGiaoDich();
    //     model.addAttribute("listGiaoDich", listGiaoDich);
    //     return "views/gdienQuanLy/giaoDichKhoaHoc";
    // }

    @GetMapping("/quanly-giao-dich")
    public String listGiaoDich(Model model) {
        List<GiaoDichKhoaHoc> listGiaoDich = giaoDichService.getAllGiaoDich();
        model.addAttribute("listGiaoDich", listGiaoDich);

        // Thêm basePath để biết là /admin hay /nhanvien
        String basePath = "admin"; // mặc định
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_NHANVIEN"))) {
            basePath = "nhanvien";
        }
        model.addAttribute("basePath", basePath); // gửi sang view

        return "views/gdienQuanLy/giaoDichKhoaHoc";
    }

    // @GetMapping("/quanly-giao-dich/{id}")
    // public String detailGiaoDich(@PathVariable Integer id, Model model) {
    //     GiaoDichKhoaHoc detail = giaoDichService.getGiaoDichById(id);
    //     model.addAttribute("gd", detail);
    //     return "views/gdienQuanLy/giaoDichKhoaHocDetail";
    // }
    @GetMapping("/quanly-giao-dich/{id}")
    public String detailGiaoDich(@PathVariable Integer id, Model model) {
        GiaoDichKhoaHoc detail = giaoDichService.getGiaoDichById(id);
        model.addAttribute("gd", detail);

        // Xác định basePath để xử lý link quay lại phù hợp
        String basePath = "admin"; // mặc định
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_NHANVIEN"))) {
            basePath = "nhanvien";
        }
        model.addAttribute("basePath", basePath);

        return "views/gdienQuanLy/giaoDichKhoaHocDetail";
    }

}