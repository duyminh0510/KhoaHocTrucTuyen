package com.duantn.controlles.controllechung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.duantn.entitys.Account;
import com.duantn.entitys.Role;
import com.duantn.repository.AccountRepository;

@Controller
public class DangKyController {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/dang-ky")
    public String hienThiFormDangKy() {
        return "views/gdienChung/register"; // hoặc đường dẫn view tương ứng
    }

    @PostMapping("/dang-ky")
    public String dangKyTaiKhoan(@RequestParam String email,
            @RequestParam String password,
            @RequestParam String hoten,
            @RequestParam String sdt,
            @RequestParam(required = false) MultipartFile avatar,
            Model model) {

        // Kiểm tra email tồn tại
        if (accountRepo.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "views/gdienChung/register";
        }

        // Tạo tài khoản mới
        Account acc = new Account();
        acc.setEmail(email);
        acc.setPassword(passwordEncoder.encode(password));
        acc.setName(hoten);
        acc.setPhone(sdt);
        acc.setStatus(true);

        // Avatar để trống tạm thời
        acc.setAvatar(null);

        // Gán role mặc định (Học viên)
        Role role = new Role();
        role.setRoleId(1); // Giả sử ID 1 là ROLE_HOCVIEN
        acc.setRole(role);

        accountRepo.save(acc);

        return "redirect:/login?register=true";
    }
}
