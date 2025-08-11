package com.duantn.controllers.controllerNhanVien;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/{prefix:(?:admin|nhanvien)}/quanly-taikhoan")
@RequiredArgsConstructor
public class QuanLyTaiKhoanController {

        private final TaiKhoanRepository taiKhoanRepository;
        private final RoleRepository roleRepository;

        @GetMapping
        public String danhSachTaiKhoan(@PathVariable String prefix, Model model) {
                Role hocVienRole = roleRepository.findByName("ROLE_HOCVIEN").orElseThrow(
                                () -> new RuntimeException("Không tìm thấy role học viên"));
                Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN").orElseThrow(
                                () -> new RuntimeException("Không tìm thấy role giảng viên"));

                List<TaiKhoan> hocVienList = taiKhoanRepository
                                .findByRoleIn(List.of(hocVienRole, giangVienRole));
                model.addAttribute("hocVienList", hocVienList);
                model.addAttribute("prefix", prefix);
                return "views/gdienQuanLy/danhsachhocvien";
        }


        @PostMapping("/toggle-status/{id}")
        public String toggleStatus(@PathVariable String prefix, @PathVariable("id") Integer id,
                        RedirectAttributes redirectAttributes) {
                TaiKhoan hocVien = taiKhoanRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));

                hocVien.setStatus(!hocVien.isStatus());
                taiKhoanRepository.save(hocVien);

                redirectAttributes.addFlashAttribute("success",
                                hocVien.isStatus() ? "Tài khoản đã được mở khóa!"
                                                : "Tài khoản đã bị khóa!");

                return "redirect:/" + prefix + "/quanly-taikhoan";
        }
}
