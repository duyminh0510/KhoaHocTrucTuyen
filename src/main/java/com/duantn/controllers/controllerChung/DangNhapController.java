package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
public class DangNhapController {

    @GetMapping("/dangnhap")
    public String loginPage(@RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "error", required = false) String error, Model model) {

        if (email != null) {
            model.addAttribute("email", email); // giữ lại email nếu có
        }

        if (error != null) {
            model.addAttribute("passwordError", "Mật khẩu không chính xác."); // luôn hiển thị lỗi
                                                                              // nếu có
        }

        return "views/gdienChung/login";
    }

    @PostMapping("/login/form")
    public String handleLogin(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Giả lập kiểm tra đăng nhập
        if ("admin@gmail.com".equals(email) && "123456".equals(password)) {
            return "redirect:/home";
        } else {
            redirectAttributes.addAttribute("email", email);
            redirectAttributes.addAttribute("error", "true");
            return "redirect:/auth/dangnhap";
        }
    }
}
