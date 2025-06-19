package com.duantn.controlles;

import com.duantn.dtos.UserRegistrationDto;
import com.duantn.entitys.Account;
import com.duantn.entitys.Role;
import com.duantn.entitys.VerificationToken;
import com.duantn.repository.AccountRepository;
import com.duantn.repository.RoleRepository;
import com.duantn.repository.VerificationTokenRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    private final JavaMailSender mailSender;
    private final VerificationTokenRepository tokenRepository;

    @SuppressWarnings("null")
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDto dto,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            result.rejectValue("email", null, "Email đã được sử dụng");
            return "register";
        }

        // Lưu tạm thông tin trong session (hoặc Redis)
        session.setAttribute("pendingUser", dto);

        // Tạo mã xác minh
        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationToken token = new VerificationToken();
        token.setEmail(dto.getEmail());
        token.setToken(code);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(token);

        // Gửi mail
        sendVerificationEmail(dto.getEmail(), code);

        return "redirect:/auth/verify";
    }

    private void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Mã xác minh đăng ký tài khoản");
        message.setText("Mã xác minh của bạn là: " + code);
        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String showVerifyForm() {
        return "verify"; // ← tên file HTML bạn đã đặt
    }

    @PostMapping("/verify")
    public String verify(@RequestParam("code") String code, HttpSession session, Model model) {
        Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(code);
        if (tokenOpt.isEmpty()) {
            model.addAttribute("error", "Mã xác minh không đúng.");
            return "verify";
        }

        VerificationToken token = tokenOpt.get();
        if (token.getExpiryTime().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Mã xác minh đã hết hạn.");
            return "verify";
        }

        UserRegistrationDto dto = (UserRegistrationDto) session.getAttribute("pendingUser");
        if (dto == null || !dto.getEmail().equals(token.getEmail())) {
            model.addAttribute("error", "Phiên làm việc không hợp lệ.");
            return "verify";
        }

        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Role not found"));

        Account account = Account.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .status(true)
                .role(studentRole)
                .build();

        accountRepository.save(account);
        tokenRepository.delete(token);
        session.removeAttribute("pendingUser");

        return "redirect:/auth/login";
    }

}
