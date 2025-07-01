package com.duantn.services;

import com.duantn.entities.VerificationToken;
import com.duantn.repositories.VerificationTokenRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    public String generateAndSendToken(String email, String name, String subject, String contentPrefix) {
        String code = String.format("%06d", new Random().nextInt(999999));

        tokenRepository.deleteByEmail(email);

        VerificationToken token = VerificationToken.builder()
                .email(email)
                .token(code)
                .expiryTime(LocalDateTime.now().plusMinutes(10))
                .build();
        tokenRepository.save(token);

        sendEmail(email, name, subject, contentPrefix, code);
        return code;
    }

    private void sendEmail(String to, String name, String subject, String contentPrefix, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("globaledu237@gmail.com", "GlobalEdu");

            if (name == null || name.isEmpty())
                name = "Quý khách";

            String html = String.format("""
                        <div style=\"font-family: Arial, sans-serif;\">
                            <h3>Xin chào %s,</h3>
                            <p>%s</p>
                            <p style=\"font-size: 22px; font-weight: bold; text-align: center;\">%s</p>
                            <p>Mã xác thực sẽ hết hạn sau <strong>10 phút</strong>.</p>
                        </div>
                    """, name, contentPrefix, code);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<VerificationToken> verifyToken(String token) {
        Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty())
            return Optional.empty();
        if (tokenOpt.get().getExpiryTime().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(tokenOpt.get());
            return Optional.empty();
        }
        return tokenOpt;
    }

    public void deleteByEmail(String email) {
        tokenRepository.deleteByEmail(email);
    }

    public void delete(VerificationToken token) {
        tokenRepository.delete(token);
    }
public String validateToken(String token) {
        Optional<VerificationToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isPresent()) {
            VerificationToken vt = tokenOpt.get();
            if (vt.getExpiryTime().isAfter(LocalDateTime.now())) {
                return vt.getEmail(); // Token hợp lệ -> trả về email
            } else {
                tokenRepository.delete(vt); // Token hết hạn -> xóa
            }
        }
        return null; // Token không hợp lệ hoặc đã hết hạn
    }

    public void deleteTokenByEmail(String email) {
        deleteByEmail(email);
    }

}