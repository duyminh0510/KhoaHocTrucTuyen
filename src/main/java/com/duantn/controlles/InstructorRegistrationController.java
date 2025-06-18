package com.duantn.controlles;

import com.duantn.dtos.GiangVienRegistrationDto;
import com.duantn.entitys.Account;
import com.duantn.entitys.GiangVien;
import com.duantn.entitys.Role;
import com.duantn.repository.AccountRepository;
import com.duantn.repository.GiangVienRepository;
import com.duantn.repository.RoleRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class InstructorRegistrationController {

    private final GiangVienRepository giangVienRepo;
    private final RoleRepository roleRepo;
    private final AccountRepository accountRepo;

    @GetMapping("/register")
    public String showInstructorForm(Model model) {
        model.addAttribute("giangVienDto", new GiangVienRegistrationDto());
        return "register-instructor";
    }

    @PostMapping("/register")
    public String registerInstructor(@ModelAttribute("giangVienDto") @Valid GiangVienRegistrationDto dto,
            BindingResult result,
            HttpSession session,
            Model model) {

        Account account = (Account) session.getAttribute("user");

        if (account == null) {
            return "redirect:/auth/login";
        }

        if (giangVienRepo.findByAccounts(account).isPresent()) {
            model.addAttribute("error", "Bạn đã đăng ký làm giảng viên rồi.");
            return "register-instructor";
        }

        // Chuyển role
        Role instructorRole = roleRepo.findByName("ROLE_INSTRUCTOR")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role ROLE_INSTRUCTOR"));

        account.setRole(instructorRole);
        accountRepo.save(account);

        GiangVien giangVien = GiangVien.builder()
                .kyNang(dto.getKyNang())
                .kinhNghiem(dto.getKinhNghiem())
                .CCCD(dto.getCCCD())
                .accounts(account)
                .build();

        giangVienRepo.save(giangVien);

        model.addAttribute("success", "Đăng ký giảng viên thành công!");
        return "redirect:/home";
    }
}
