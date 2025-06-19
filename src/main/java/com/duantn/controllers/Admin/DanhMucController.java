package com.duantn.controllers.Admin;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.duantn.entities.DanhMuc;

@Controller
public class DanhMucController {

    @GetMapping("/admin/x")
    public String hienThiDanhMuc(Model model) {
        // Mock dữ liệu để hiển thị giao diện
        List<DanhMuc> danhSachDanhMuc = Arrays.asList(
                DanhMuc.builder().danhmucId(1).tenDanhMuc("Lập trình Java")
                        .ngayTao(LocalDateTime.now().minusDays(10)).ngayCapNhat(LocalDateTime.now())
                        .build(),
                DanhMuc.builder().danhmucId(2).tenDanhMuc("Thiết kế Web")
                        .ngayTao(LocalDateTime.now().minusDays(5)).ngayCapNhat(LocalDateTime.now())
                        .build());

        model.addAttribute("danhMuc", new DanhMuc()); // để form thêm mới hoạt động
        model.addAttribute("danhSachDanhMuc", danhSachDanhMuc); // danh sách mock

        return "views/Admin/DanhMuc"; // đường dẫn tới file HTML
    }
}
