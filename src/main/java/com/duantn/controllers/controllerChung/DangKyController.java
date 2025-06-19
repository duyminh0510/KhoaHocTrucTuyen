package com.duantn.controllers.controllerChung;

import com.duantn.dtos.DangKyHocVienDto;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.VerificationToken;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.repositories.VerificationTokenRepository;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class DangKyController {

    private final TaiKhoanRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    private final VerificationTokenRepository tokenRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new DangKyHocVienDto());
        return "views/gdienChung/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid DangKyHocVienDto dto,
            BindingResult result,
            HttpSession session,
            Model model) {
        if (accountRepository.existsByEmail(dto.getEmail())) {
            result.rejectValue("email", "email.exists", "Email đã được sử dụng");
            return "register";
        }

        session.setAttribute("pendingUser", dto);

        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationToken token = new VerificationToken();
        token.setEmail(dto.getEmail());
        token.setToken(code);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(10));
        tokenRepository.save(token);

        sendVerificationEmail(dto.getEmail(), dto.getName(), code);

        return "redirect:/auth/verify";
    }

    private void sendVerificationEmail(String to, String name, String code) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Xác minh tài khoản - GlobalEdu");
            helper.setFrom("globaledu237@gmail.com", "GlobalEdu");

            if (name == null || name.trim().isEmpty()) {
                name = "Quý khách";
            }

            String html = String.format(
                    """
                            <div style="font-family: Arial, sans-serif; line-height: 1.6;">
                                <h2>Xin chào %s,</h2>
                                <p>Cảm ơn bạn đã đăng ký tài khoản tại <strong>GlobalEdu</strong>.</p>
                                <p>Mã xác minh của bạn là:</p>
                                <div style="font-size: 24px; font-weight: bold; color: #2c3e50; text-align: center; margin: 20px 0;">%s</div>
                                <p>Mã xác minh sẽ hết hạn sau <strong>10 phút</strong>. Vui lòng không chia sẻ mã này với người khác.</p>
                                <p>Nếu bạn không thực hiện hành động này, vui lòng bỏ qua email này.</p>
                                <br>
                                <p>Trân trọng,<br><strong>Đội ngũ GlobalEdu</strong></p>
                                <hr>
                                <p style="font-size: 12px; color: gray;">Email này được gửi tự động. Vui lòng không phản hồi.</p>
                            </div>
                            """,
                    name, code);

            helper.setText(html, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Lỗi khi gửi email xác minh: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @GetMapping("/verify")
    public String showVerifyForm() {
        return "verify";
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

        DangKyHocVienDto dto = (DangKyHocVienDto) session.getAttribute("pendingUser");
        if (dto == null || !dto.getEmail().equals(token.getEmail())) {
            model.addAttribute("error", "Phiên làm việc không hợp lệ.");
            return "verify";
        }

        Role studentRole = roleRepository.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        TaiKhoan account = TaiKhoan.builder()
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

        return "redirect:/login";
    }
}
