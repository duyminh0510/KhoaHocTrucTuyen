package com.duantn.controlles.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileGiangVien {
    @GetMapping("/admin/profileGiangVien")
    public String profileGiangVien() {
        return "views/Admin/ProfileGiangVien"; // Tìm đến:
                                               // templates/admin/profile-giang-vien.html
    }
}
