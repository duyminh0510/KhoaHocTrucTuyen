package com.duantn.controllers.Admin;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.duantn.entities.Account;
import com.duantn.entities.Role;

@Controller
public class NhanVienController {

    @GetMapping("/admin/nhanvien")
    public String hienThiNhanVien(Model model) {
        // Tạo role demo
        Role roleNhanVien = Role.builder().roleId(2).name("Nhân viên").build();

        // Tạo danh sách nhân viên demo
        Account nv1 = Account.builder().accountId(1).name("Nguyễn Văn A").email("a@example.com")
                .phone("0909999999").avatar("https://i.pravatar.cc/150?img=1").status(true)
                .role(roleNhanVien).build();

        Account nv2 = Account.builder().accountId(2).name("Trần Thị B").email("b@example.com")
                .phone("0911222333").avatar("https://i.pravatar.cc/150?img=2").status(false)
                .role(roleNhanVien).build();

        List<Account> danhSachNhanVien = Arrays.asList(nv1, nv2);

        model.addAttribute("dsNhanVien", danhSachNhanVien);
        return "views/Admin/NhanVien"; // Trả về view hiển thị danh sách nhân viên
    }
}
