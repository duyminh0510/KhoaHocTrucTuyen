package com.duantn.controllers.controllerNhanVien;

import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/{prefix:(?:admin|nhanvien)}/quanly-giangvien")
@RequiredArgsConstructor
public class QuanLyGiangVienController {

        private final TaiKhoanRepository taiKhoanRepository;
        private final RoleRepository roleRepository;

        @GetMapping
        public String danhSach(@PathVariable String prefix, Model model) {
                Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));

                List<TaiKhoan> giangVienList = taiKhoanRepository.findByRole(giangVienRole);
                model.addAttribute("giangVienList", giangVienList);
                model.addAttribute("prefix", prefix);
                return "views/gdienQuanLy/danhsachgiangvien";
        }

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