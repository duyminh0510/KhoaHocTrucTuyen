package com.duantn.controllers.controllerNhanVien;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping({ "/admin", "/nhanvien" })
@PreAuthorize("hasAnyRole('ADMIN', 'NHANVIEN')")
public class HocVienController {

    public static class HocVienVM {
        public Integer id;
        public String name;
        public String email;
        public String phone;
        public String ngayTao;
        public String trangThai;
        public String avatar;

        public HocVienVM(Integer id, String name, String email, String phone, String ngayTao, String trangThai,
                String avatar) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.phone = phone;
            this.ngayTao = ngayTao;
            this.trangThai = trangThai;
            this.avatar = avatar;
        }
    }

    private List<HocVienVM> listHocVien() {
        return Arrays.asList(
                new HocVienVM(1, "Nguyễn Văn A", "a@gmail.com", "0123456789", "01/06/2024", "Hoạt động",
                        "https://randomuser.me/api/portraits/men/1.jpg"),
                new HocVienVM(2, "Trần Thị B", "b@gmail.com", "0987654321", "02/06/2024", "Khoá",
                        "https://randomuser.me/api/portraits/women/2.jpg"),
                new HocVienVM(3, "Lê Văn C", "c@gmail.com", "0912345678", "03/06/2024", "Hoạt động",
                        "https://randomuser.me/api/portraits/men/3.jpg"));
    }

    @GetMapping("/quanly-hocvien")
    public String listHocVien(Model model) {
        model.addAttribute("listHocVien", listHocVien());
        return "views/gdienQuanLy/hocVien";
    }

    @GetMapping("/quanly-hocvien-detail/{id}")
    public String detailHocVien(@PathVariable Integer id, Model model) {
        HocVienVM detail = null;
        for (HocVienVM hv : listHocVien()) {
            if (hv.id.equals(id)) {
                detail = hv;
                break;
            }
        }
        model.addAttribute("hv", detail);
        return "views/gdienQuanLy/hocVienDetail";
    }

    // Hiển thị form thêm học viên
    @GetMapping("/quanly-hocvien-form")
    public String addHocVienForm(Model model) {
        model.addAttribute("formTitle", "Thêm học viên");
        return "views/gdienQuanLy/hocVienForm";
    }

    // Hiển thị form sửa học viên
    @GetMapping("/quanly-hocvien-form/{id}")
    public String editHocVienForm(@PathVariable Integer id, Model model) {
        model.addAttribute("formTitle", "Sửa học viên");
        // Có thể truyền thêm dữ liệu học viên vào model nếu cần
        return "views/gdienQuanLy/hocVienForm";
    }
}