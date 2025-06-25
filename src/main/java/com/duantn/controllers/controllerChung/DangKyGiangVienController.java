package com.duantn.controllers.controllerChung;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.dtos.DangKyGiangVienDto;
import com.duantn.entities.GiangVien;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/instructor")
public class DangKyGiangVienController {

    private final GiangVienRepository giangVienRepo;
    private final RoleRepository roleRepo;
    private final TaiKhoanRepository accountRepo;

    @GetMapping("/register")
    public String showInstructorForm(Model model) {
        model.addAttribute("giangVienDto", new DangKyGiangVienDto());
        return "register-instructor";
    }

    @PostMapping("/register")
    public String registerInstructor(@ModelAttribute("giangVienDto") @Valid DangKyGiangVienDto dto,
            BindingResult result, HttpSession session, Model model) {

        TaiKhoan account = (TaiKhoan) session.getAttribute("user");

        if (account == null) {
            return "redirect:/auth/login";
        }

        if (giangVienRepo.findByTaikhoan(account).isPresent()) {
            model.addAttribute("error", "Bạn đã đăng ký làm giảng viên rồi.");
            return "register-instructor";
        }

        // Chuyển role
        Role instructorRole = roleRepo.findByName("ROLE_INSTRUCTOR")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role ROLE_INSTRUCTOR"));

        account.setRole(instructorRole);
        accountRepo.save(account);

        GiangVien giangVien = GiangVien.builder().kyNang(dto.getKyNang())
                .kinhNghiem(dto.getKinhNghiem()).CCCD(dto.getCCCD()).taikhoan(account).build();

        giangVienRepo.save(giangVien);

        model.addAttribute("success", "Đăng ký giảng viên thành công!");
        return "redirect:/home";
    }
}
