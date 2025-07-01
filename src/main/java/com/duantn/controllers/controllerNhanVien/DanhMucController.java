package com.duantn.controllers.controllerNhanVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.DanhMuc;
import com.duantn.services.DanhMucService;

@Controller
@RequestMapping("/danhmuc")
public class DanhMucController {
     @Autowired
    private DanhMucService service;

    // Hiển thị danh sách + form
    @GetMapping
    public String trangDanhSach(Model model) {
        List<DanhMuc> danhmucs = service.layTatCa();
        model.addAttribute("danhmucs", danhmucs);
        model.addAttribute("danhmuc", new DanhMuc());
        return "views/gdienQuanLy/danhmuc";
    }

    // Thêm mới
    @PostMapping("/add")
    public String them(@ModelAttribute("danhmuc") DanhMuc danhMuc, Model model) {
        try {
            service.taoDanhMuc(danhMuc);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("danhmucs", service.layTatCa());
            return "views/gdienQuanLy/danhmuc"; 
        }
        return "redirect:/danhmuc";
    }

    // Chuyển đến form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String suaForm(@PathVariable Integer id, Model model) {
        DanhMuc dm = service.layTheoId(id);
        model.addAttribute("danhmucs", service.layTatCa());
        model.addAttribute("danhmuc", dm);
        return "views/gdienQuanLy/danhmuc";
    }

    // Cập nhật
    @PostMapping("/edit")
    public String capNhat(@ModelAttribute("danhmuc") DanhMuc danhMuc, Model model) {
        if (service.daTonTaiTenKhacId(danhMuc.getTenDanhMuc(), danhMuc.getDanhmucId())) {
            model.addAttribute("error", "Tên danh mục đã tồn tại!");
            model.addAttribute("danhmucs", service.layTatCa());
            model.addAttribute("danhmuc", danhMuc);
            return "views/gdienQuanLy/danhmuc"; 
        }

        service.capNhat(danhMuc.getDanhmucId(), danhMuc);
        return "redirect:/danhmuc";
    }

    // Xóa
    @PostMapping("/vohieuhoa/{id}")
    public String xoa(@PathVariable Integer id) {
        service.voHieuHoa(id);
        return "redirect:/danhmuc";
    }

    @PostMapping("/kichhoat/{id}")
    public String kichHoat(@PathVariable Integer id) {
        service.kichHoat(id);
        return "redirect:/danhmuc";
    }
}

