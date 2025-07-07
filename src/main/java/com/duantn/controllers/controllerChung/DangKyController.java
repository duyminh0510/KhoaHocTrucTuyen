package com.duantn.controllers.controllerChung;

import com.duantn.dtos.DangKyHocVienDto;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.VerificationToken;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.TokenService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class DangKyController {

    private final TaiKhoanRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model, HttpSession session) {
        model.addAttribute("user", new DangKyHocVienDto());
        return "views/gdienChung/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid DangKyHocVienDto dto,
                                BindingResult result,
                                HttpSession session,
                                Model model) {

        if (result.hasErrors())
            return "views/gdienChung/register";

        if (accountRepository.existsByEmail(dto.getEmail())) {
            result.rejectValue("email", "email.exists", "Email đã được sử dụng");
            return "views/gdienChung/register";
        }

        session.setAttribute("pendingUser", dto);
        tokenService.generateAndSendToken(dto.getEmail(), dto.getName(), "Xác minh tài khoản",
                "Mã xác minh của bạn là:");
        return "redirect:/auth/verify?type=register";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "views/gdienChung/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam("email") String email, HttpSession session, Model model) {
        Optional<TaiKhoan> tk = accountRepository.findByEmail(email);
        if (tk.isEmpty()) {
            model.addAttribute("error", "Email không tồn tại");
            return "views/gdienChung/forgot-password";
        }
        session.setAttribute("resetEmail", email);
        tokenService.generateAndSendToken(email, tk.get().getName(), "Khôi phục mật khẩu",
                "Mã xác minh khôi phục mật khẩu:");
        return "redirect:/auth/verify?type=reset";
    }

    @GetMapping("/verify")
    public String showVerifyForm(Model model, @RequestParam(value = "type", defaultValue = "register") String type) {
        model.addAttribute("type", type);
        return "views/gdienChung/verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam("code") String code,
                         @RequestParam(value = "type", required = false) String type,
                         HttpSession session,
                         Model model) {


        Optional<VerificationToken> tokenOpt = tokenService.verifyToken(code);
        if (tokenOpt.isEmpty()) {
            model.addAttribute("error", "Mã xác minh không đúng hoặc đã hết hạn.");
            model.addAttribute("type", type != null ? type : "register");
            return "views/gdienChung/verify";
        }

        VerificationToken token = tokenOpt.get();

        // Kiểm tra hạn sử dụng mã
        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Mã xác minh đã hết hạn.");
            model.addAttribute("type", type != null ? type : "register");
            return "views/gdienChung/verify";
        }

        // Xử lý xác minh đăng ký
        DangKyHocVienDto pending = (DangKyHocVienDto) session.getAttribute("pendingUser");
        if (pending != null && pending.getEmail().equals(token.getEmail())) {
            Role studentRole = roleRepository.findByName("ROLE_HOCVIEN")
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

            TaiKhoan account = TaiKhoan.builder()
                    .name(pending.getName())
                    .email(pending.getEmail())
                    .phone(pending.getPhone())
                    .password(passwordEncoder.encode(pending.getPassword()))
                    .status(true)
                    .role(studentRole)
                    .build();

            accountRepository.save(account);
            tokenService.delete(token);
            session.removeAttribute("pendingUser");
            return "redirect:/auth/login";
        }

        // Xử lý xác minh khôi phục mật khẩu
        String resetEmail = (String) session.getAttribute("resetEmail");
        if (resetEmail != null && resetEmail.equals(token.getEmail())) {
            session.setAttribute("verifiedEmail", resetEmail);
            session.removeAttribute("resetEmail");
            tokenService.delete(token);
            return "redirect:/auth/reset-password";
        }

        model.addAttribute("error", "Phiên xác minh không hợp lệ.");
        model.addAttribute("type", type != null ? type : "register");
        return "views/gdienChung/verify";
    }

    @GetMapping("/reset-password")
    public String showResetForm(HttpSession session) {
        if (session.getAttribute("verifiedEmail") == null) {
            return "redirect:/auth/forgot-password";
        }
        return "views/gdienChung/reset-password";
    }

    @PostMapping("/reset-password")
    public String handleReset(@RequestParam("password") String password, HttpSession session) {
        String email = (String) session.getAttribute("verifiedEmail");
        if (email == null) {
            return "redirect:/auth/forgot-password";
        }

        TaiKhoan tk = accountRepository.findByEmail(email).orElseThrow();
        tk.setPassword(passwordEncoder.encode(password));
        accountRepository.save(tk);
        session.removeAttribute("verifiedEmail");

        return "redirect:/auth/login?resetSuccess";
    }
}
