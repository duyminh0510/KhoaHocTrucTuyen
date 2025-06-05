package com.duantn.controlles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duantn.entitys.Account;
import com.duantn.services.AccountService;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;

    // Form đăng nhập
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, Model model,
            HttpSession session) {
        Account account = accountService.login(email, password);
        if (account != null) {
            session.setAttribute("user", account);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@ModelAttribute("account") Account account, Model model) {
        Account created = accountService.register(account);
        if (created == null) {
            model.addAttribute("error", "Email đã tồn tại!");
            return "register";
        }
        return "redirect:/login"; // Sau khi đăng ký thành công
    }


    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

}
