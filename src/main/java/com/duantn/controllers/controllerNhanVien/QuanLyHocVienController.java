package com.duantn.controllers.controllerNhanVien;

import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/{prefix:(?:admin|nhanvien)}/quanly-hocvien")
@RequiredArgsConstructor
public class QuanLyHocVienController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final RoleRepository roleRepository;

    // ✅ Danh sách học viên
    @GetMapping
    public String danhSachHocVien(@PathVariable String prefix, Model model) {
        Role hocVienRole = roleRepository.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role học viên"));

        List<TaiKhoan> hocVienList = taiKhoanRepository.findByRole(hocVienRole);
        model.addAttribute("hocVienList", hocVienList);
        model.addAttribute("prefix", prefix); // để dùng lại trong link edit, save, v.v.
        return "views/gdienQuanLy/danhsachhocvien";
    }

    // ✅ Form edit
    @GetMapping("/edit/{id}")
    public String editHocVien(@PathVariable String prefix,
            @PathVariable("id") Integer id,
            Model model) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));
        model.addAttribute("taiKhoan", taiKhoan);
        model.addAttribute("prefix", prefix);
        return "views/gdienQuanLy/formhocvien";
    }

    // ✅ Save
    @PostMapping("/save")
    public String saveHocVien(
            @ModelAttribute("taiKhoan") @Valid TaiKhoan form,
            BindingResult bindingResult,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            @RequestParam("prefix") String prefix,
            RedirectAttributes redirectAttributes) {

        if (form.getTaikhoanId() == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm học viên mới.");
            return "redirect:/" + prefix + "/quanly-hocvien";
        }

        if (bindingResult.hasErrors()) {
            return "views/gdienQuanLy/formhocvien";
        }

        TaiKhoan existing = taiKhoanRepository.findById(form.getTaikhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        existing.setName(form.getName());
        existing.setEmail(form.getEmail());
        existing.setPhone(form.getPhone());
        existing.setStatus(form.isStatus());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                existing.setAvatar(saveAvatarFile(avatarFile));
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu avatar!");
                return "redirect:/" + prefix + "/quanly-hocvien";
            }
        }

        taiKhoanRepository.save(existing);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/" + prefix + "/quanly-hocvien";
    }

    // ✅ Khóa / mở khóa
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable String prefix,
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        TaiKhoan hocVien = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));

        hocVien.setStatus(!hocVien.isStatus());
        taiKhoanRepository.save(hocVien);

        redirectAttributes.addFlashAttribute("success", hocVien.isStatus()
                ? "Tài khoản đã được mở khóa!"
                : "Tài khoản đã bị khóa!");

        return "redirect:/" + prefix + "/quanly-hocvien";
    }

    // 👤 Hàm giả lập lưu avatar
    private String saveAvatarFile(MultipartFile file) throws IOException {
        return file.getOriginalFilename(); // nên thay bằng xử lý lưu thực
    }
}
