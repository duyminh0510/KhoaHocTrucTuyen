package com.duantn.controllers.controllerNhanVien;

import com.duantn.entities.GiangVien;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auth/giang-vien")
@RequiredArgsConstructor
public class GiangVienController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final RoleRepository roleRepository;

    // 📄 Hiển thị danh sách giảng viên
    @GetMapping
    public String list(Model model) {
        Role role = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));

        List<TaiKhoan> giangVienList = taiKhoanRepository.findByRole(role);
        model.addAttribute("giangVienList", giangVienList);
        return "views/gdienQuanLy/danhsachgiangvien";
    }

    // ✏️ Hiển thị form chỉnh sửa giảng viên
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        Optional<TaiKhoan> tkOpt = taiKhoanRepository.findById(id);
        if (tkOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy giảng viên!");
            return "redirect:/auth/giang-vien";
        }

        TaiKhoan tk = tkOpt.get();
        if (tk.getGiangVien() == null) {
            tk.setGiangVien(new GiangVien());
        }
        model.addAttribute("taiKhoan", tk);
        return "views/gdienQuanLy/formgiangvien";
    }

    // 💾 Lưu chỉnh sửa giảng viên
    @PostMapping("/save")
    public String updateGiangVien(
            @ModelAttribute("taiKhoan") @Valid TaiKhoan taiKhoanForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (taiKhoanForm.getTaikhoanId() == null) {
            redirectAttributes.addFlashAttribute("error", "Không cho phép thêm giảng viên mới.");
            return "redirect:/auth/giang-vien";
        }

        if (bindingResult.hasErrors()) {
            return "views/gdienQuanLy/formgiangvien";
        }

        Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));

        TaiKhoan taiKhoanToUpdate = taiKhoanRepository.findById(taiKhoanForm.getTaikhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        String oldPassword = taiKhoanToUpdate.getPassword();

        taiKhoanToUpdate.setName(taiKhoanForm.getName());
        taiKhoanToUpdate.setEmail(taiKhoanForm.getEmail());
        taiKhoanToUpdate.setPhone(taiKhoanForm.getPhone());
        taiKhoanToUpdate.setAvatar(taiKhoanForm.getAvatar());
        taiKhoanToUpdate.setStatus(taiKhoanForm.isStatus());
        taiKhoanToUpdate.setPassword(oldPassword);
        taiKhoanToUpdate.setRole(giangVienRole);

        GiangVien gv = taiKhoanToUpdate.getGiangVien();
        if (gv == null) {
            gv = new GiangVien();
            gv.setTaikhoan(taiKhoanToUpdate);
        }

        GiangVien gvForm = taiKhoanForm.getGiangVien();
        if (gvForm != null) {
            gv.setKyNang(gvForm.getKyNang());
            gv.setKinhNghiem(gvForm.getKinhNghiem());
            gv.setCCCD(gvForm.getCCCD());
            gv.setCongViec(gvForm.getCongViec());

            if (gvForm.getNgaySinh() != null) {
                gv.setNgaySinh(gvForm.getNgaySinh());
            }

            gv.setGioiTinh(gvForm.getGioiTinh());
            gv.setChuyenNganh(gvForm.getChuyenNganh());
        }

        taiKhoanToUpdate.setGiangVien(gv);
        taiKhoanRepository.save(taiKhoanToUpdate);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin giảng viên thành công!");
        return "redirect:/auth/giang-vien";
    }

    // 🔒 Khoá / mở khóa tài khoản giảng viên
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable("id") Integer id, RedirectAttributes ra) {
        TaiKhoan giangVien = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));

        giangVien.setStatus(!giangVien.isStatus());
        taiKhoanRepository.save(giangVien);

        ra.addFlashAttribute("success", giangVien.isStatus()
                ? "Tài khoản đã được mở khóa!"
                : "Tài khoản đã bị khóa!");

        return "redirect:/auth/giang-vien";
    }
}
