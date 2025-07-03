package com.duantn.controllers.controllerNhanVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.duantn.entities.DanhMuc;
import com.duantn.services.DanhMucService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class DanhMucController {

    @Autowired
    private DanhMucService service;

    private String getPrefix(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/admin") ? "/admin" : "/nhanvien";
    }

    @GetMapping("/danhmuc")
    public String trangDanhSach(HttpServletRequest request, Model model) {
        List<DanhMuc> danhmucs = service.layTatCa();
        model.addAttribute("danhmucs", danhmucs);
        model.addAttribute("danhmuc", new DanhMuc());
        model.addAttribute("prefixPath", getPrefix(request));
        return "views/gdienQuanLy/danhmuc";
    }

    @PostMapping("/danhmuc/add")
    public String them(@ModelAttribute("danhmuc") DanhMuc danhMuc, HttpServletRequest request, Model model) {
        try {
            service.taoDanhMuc(danhMuc);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("danhmucs", service.layTatCa());
            model.addAttribute("danhmuc", danhMuc);
            model.addAttribute("prefixPath", getPrefix(request));
            return "views/gdienQuanLy/danhmuc";
        }
        return "redirect:" + getPrefix(request) + "/danhmuc";
    }

    @GetMapping("/danhmuc/edit/{id}")
    public String suaForm(@PathVariable Integer id, HttpServletRequest request, Model model) {
        DanhMuc dm = service.layTheoId(id);
        model.addAttribute("danhmucs", service.layTatCa());
        model.addAttribute("danhmuc", dm);
        model.addAttribute("prefixPath", getPrefix(request));
        return "views/gdienQuanLy/danhmuc";
    }

    @PostMapping("/danhmuc/edit")
    public String capNhat(@ModelAttribute("danhmuc") DanhMuc danhMuc, HttpServletRequest request, Model model) {
        if (service.daTonTaiTenKhacId(danhMuc.getTenDanhMuc(), danhMuc.getDanhmucId())) {
            model.addAttribute("error", "Tên danh mục đã tồn tại!");
            model.addAttribute("danhmucs", service.layTatCa());
            model.addAttribute("danhmuc", danhMuc);
            model.addAttribute("prefixPath", getPrefix(request));
            return "views/gdienQuanLy/danhmuc";
        }

        service.capNhat(danhMuc.getDanhmucId(), danhMuc);
        return "redirect:" + getPrefix(request) + "/danhmuc";
    }

    @PostMapping("/vohieuhoa/{id}")
    public String xoa(@PathVariable Integer id, HttpServletRequest request) {
        service.voHieuHoa(id);
        return "redirect:" + getPrefix(request) + "/danhmuc";
    }

    @PostMapping("/kichhoat/{id}")
    public String kichHoat(@PathVariable Integer id, HttpServletRequest request) {
        service.kichHoat(id);
        return "redirect:" + getPrefix(request) + "/danhmuc";
    }
}

