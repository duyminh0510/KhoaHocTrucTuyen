package com.duantn.controlles;

import com.duantn.entitys.Account;
import com.duantn.services.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            HttpSession session) {

        Account account = accountService.login(email, password);

        if (account != null) {
            session.setAttribute("user", account);
            return "redirect:/home";
        }

        model.addAttribute("error", "Email hoặc mật khẩu không đúng!");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}
