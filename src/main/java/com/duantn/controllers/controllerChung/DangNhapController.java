package com.duantn.controllers.controllerChung;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.TaiKhoanService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class DangNhapController {

    @Autowired
    private TaiKhoanService taiKhoanService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Trang login
    @GetMapping("/dangnhap")
    public String loginPage(Model model,
            @RequestParam(value = "email", required = false) String email) {
        model.addAttribute("email", email);
        return "views/gdienChung/login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login/form")
    public String loginForm(@RequestParam String email, @RequestParam String password,
            HttpSession session, RedirectAttributes redirectAttributes) {

        TaiKhoan taiKhoan = taiKhoanService.findByEmail(email);

        if (taiKhoan == null) {
            redirectAttributes.addFlashAttribute("error", "Email không tồn tại.");
            return "redirect:/auth/dangnhap?email=" + email;
        }

        if (!passwordEncoder.matches(password, taiKhoan.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Sai mật khẩu.");
            return "redirect:/auth/dangnhap?email=" + email;
        }

        // Đăng nhập thành công
        session.setAttribute("user", taiKhoan);
        return "redirect:/"; // Trang chính sau khi đăng nhập
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addAttribute("logout", true);
        return "redirect:/auth/dangnhap";
    }
}
