package com.duantn.controllers.controllerAdmin;

// import com.duantn.entities.ThongBao;
// import com.duantn.services.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/quanly-thong-bao-he-thong")
public class ThongBaoHThongController {

    @Autowired
    // private ThongBaoService thongBaoService;

    // Hiển thị danh sách thông báo hệ thống
    @GetMapping("")
    public String danhSachThongBao() {
        // List<ThongBao> listThongBao = thongBaoService.getAllThongBao();
        // model.addAttribute("listThongBao", listThongBao);
        return "views/gdienQuanLy/quanly-thong-bao-he-thong";
    }

    // Hiển thị form tạo mới
    @GetMapping("/new")
    public String hienThiFormTaoMoi() {
        // model.addAttribute("pageTitle", "Tạo Thông Báo Mới");
        // model.addAttribute("thongBao", new ThongBao());
        return "views/gdienQuanLy/form-thong-bao";
    }

    // Hiển thị form sửa
    @GetMapping("/edit/{id}")
    public String hienThiFormSua(@PathVariable("id") Integer id) {
        // ThongBao thongBao = thongBaoService.findById(id);
        // model.addAttribute("pageTitle", "Sửa Thông Báo");
        // model.addAttribute("thongBao", thongBao);
        return "views/gdienQuanLy/form-thong-bao";
    }

    // Xem chi tiết
    @GetMapping("/view/{id}")
    public String xemChiTiet(@PathVariable("id") Integer id) {
        // ThongBao thongBao = thongBaoService.findById(id);
        // model.addAttribute("thongBao", thongBao);
        return "views/gdienQuanLy/xem-thong-bao";
    }
}
