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
@RequestMapping("/auth/hoc-vien")
@RequiredArgsConstructor
public class HocVienController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final RoleRepository roleRepository;

    // 🧾 Danh sách tất cả học viên (kể cả bị khóa)
    @GetMapping
    public String danhSachHocVien(Model model) {
        Role hocVienRole = roleRepository.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role học viên"));

        List<TaiKhoan> hocVienList = taiKhoanRepository.findByRole(hocVienRole);
        model.addAttribute("hocVienList", hocVienList);
        return "views/gdienQuanLy/danhsachhocvien";
    }

    // ✏️ Form chỉnh sửa học viên
    @GetMapping("/edit/{id}")
    public String editHocVien(@PathVariable("id") Integer id, Model model) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));
        model.addAttribute("taiKhoan", taiKhoan);
        return "views/gdienQuanLy/formhocvien";
    }

    // 💾 Lưu cập nhật học viên
    @PostMapping("/save")
    public String updateHocVien(
            @ModelAttribute("taiKhoan") @Valid TaiKhoan taiKhoanForm,
            BindingResult bindingResult,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        if (taiKhoanForm.getTaikhoanId() == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm học viên mới.");
            return "redirect:/auth/hoc-vien";
        }

        if (bindingResult.hasErrors()) {
            return "views/gdienQuanLy/formhocvien";
        }

        TaiKhoan taiKhoanToUpdate = taiKhoanRepository.findById(taiKhoanForm.getTaikhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        taiKhoanToUpdate.setName(taiKhoanForm.getName());
        taiKhoanToUpdate.setEmail(taiKhoanForm.getEmail());
        taiKhoanToUpdate.setPhone(taiKhoanForm.getPhone());
        taiKhoanToUpdate.setStatus(taiKhoanForm.isStatus());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                taiKhoanToUpdate.setAvatar(saveAvatarFile(avatarFile));
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu avatar!");
                return "redirect:/auth/hoc-vien";
            }
        }

        taiKhoanRepository.save(taiKhoanToUpdate);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin học viên thành công!");
        return "redirect:/auth/hoc-vien";
    }

    // 🔒 Khóa/Mở khóa tài khoản học viên
    @PostMapping("/toggle-status/{id}")
    public String toggleHocVienStatus(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        TaiKhoan hocVien = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));

        hocVien.setStatus(!hocVien.isStatus());
        taiKhoanRepository.save(hocVien);

        redirectAttributes.addFlashAttribute("success", hocVien.isStatus()
                ? "Tài khoản đã được mở khóa!"
                : "Tài khoản đã bị khóa!");

        return "redirect:/auth/hoc-vien";
    }

    // 👤 Hàm giả lập lưu avatar
    private String saveAvatarFile(MultipartFile file) throws IOException {
        // Giả lập tên file (nên thay bằng xử lý upload thực sự)
        return file.getOriginalFilename();
    }
}
