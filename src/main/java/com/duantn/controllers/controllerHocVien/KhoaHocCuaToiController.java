package com.duantn.controllers.controllerHocVien;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duantn.entities.DangHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.DangHocRepository;
import com.duantn.repositories.TaiKhoanRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KhoaHocCuaToiController {

        private final DangHocRepository dangHocRepository;
        private final TaiKhoanRepository taiKhoanRepository;

        @GetMapping("/khoa-hoc-cua-toi")
        public String khoaHocCuaToi(Model model, Principal principal) {
                String email = principal.getName(); // Spring Security đảm bảo đây là email

                TaiKhoan taikhoan = taiKhoanRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy tài khoản với email: " + email));

                List<DangHoc> dangHocList = dangHocRepository.findByTaikhoanIdWithKhoaHoc(taikhoan.getTaikhoanId());

                List<DangHoc> daNhanChungChi = dangHocList.stream()
                                .filter(DangHoc::isDaCap_ChungChi)
                                .toList();

                model.addAttribute("tatCaDangKy", dangHocList);
                model.addAttribute("chungChi", daNhanChungChi);

                return "views/gdienHocVien/khoa-hoc-cua-toi";
        }

}
