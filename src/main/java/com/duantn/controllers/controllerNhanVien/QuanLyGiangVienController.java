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
@RequestMapping("/{prefix:(?:admin|nhanvien)}/quanly-giangvien")
@RequiredArgsConstructor
public class QuanLyGiangVienController {

    private final TaiKhoanRepository taiKhoanRepository;
    private final RoleRepository roleRepository;

    // 📋 Danh sách giảng viên
    @GetMapping
    public String danhSach(@PathVariable String prefix, Model model) {
        Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));

        List<TaiKhoan> giangVienList = taiKhoanRepository.findByRole(giangVienRole);
        model.addAttribute("giangVienList", giangVienList);
        model.addAttribute("prefix", prefix);
        return "views/gdienQuanLy/danhsachgiangvien";
    }

    // ✏️ Form chỉnh sửa giảng viên
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String prefix,
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes ra) {

        Optional<TaiKhoan> tkOpt = taiKhoanRepository.findById(id);
        if (tkOpt.isEmpty()) {
            ra.addFlashAttribute("error", "Không tìm thấy giảng viên!");
            return "redirect:/" + prefix + "/quanly-giangvien";
        }

        TaiKhoan tk = tkOpt.get();
        if (tk.getGiangVien() == null) {
            tk.setGiangVien(new GiangVien());
        }
        System.out.println("TK ID: " + tk.getTaikhoanId());

        model.addAttribute("taiKhoan", tk);
        model.addAttribute("prefix", prefix);
        return "views/gdienQuanLy/formgiangvien";
    }

    // 💾 Lưu chỉnh sửa
    @PostMapping("/save")
    public String update(@PathVariable String prefix,
            @ModelAttribute("taiKhoan") @Valid TaiKhoan taiKhoanForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (taiKhoanForm.getTaikhoanId() == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm mới giảng viên.");
            return "redirect:/" + prefix + "/quanly-giangvien";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("prefix", prefix);
            return "views/gdienQuanLy/formgiangvien";
        }

        Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));

        TaiKhoan existing = taiKhoanRepository.findById(taiKhoanForm.getTaikhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        existing.setName(taiKhoanForm.getName());
        existing.setEmail(taiKhoanForm.getEmail());
        existing.setPhone(taiKhoanForm.getPhone());
        existing.setAvatar(taiKhoanForm.getAvatar());
        existing.setStatus(taiKhoanForm.isStatus());
        existing.setPassword(existing.getPassword()); // Giữ nguyên
        existing.setRole(giangVienRole);

        GiangVien gv = existing.getGiangVien();
        if (gv == null) {
            gv = new GiangVien();
        }

        GiangVien form = taiKhoanForm.getGiangVien();
        if (form != null) {
            gv.setKyNang(form.getKyNang());
            gv.setKinhNghiem(form.getKinhNghiem());
            gv.setCCCD(form.getCCCD());
            gv.setCongViec(form.getCongViec());
            gv.setNgaySinh(form.getNgaySinh());
            gv.setGioiTinh(form.getGioiTinh());
            gv.setChuyenNganh(form.getChuyenNganh());
        }

        gv.setTaikhoan(existing);
        existing.setGiangVien(gv);
        taiKhoanRepository.save(existing);

        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin giảng viên thành công!");
        return "redirect:/" + prefix + "/quanly-giangvien";
    }

    // 🔒 Khoá/Mở khoá tài khoản
    @PostMapping("/toggle-status/{id}")
    public String toggleStatus(@PathVariable String prefix,
            @PathVariable("id") Integer id,
            RedirectAttributes ra) {

        TaiKhoan giangVien = taiKhoanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));

        giangVien.setStatus(!giangVien.isStatus());
        taiKhoanRepository.save(giangVien);

        ra.addFlashAttribute("success", giangVien.isStatus()
                ? "Tài khoản đã được mở khóa!"
                : "Tài khoản đã bị khóa!");

        return "redirect:/" + prefix + "/quanly-giangvien";
    }
}
