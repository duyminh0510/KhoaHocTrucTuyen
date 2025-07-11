package com.duantn.controllers.controllerHocVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.duantn.dtos.GiaoDichRequest;
import com.duantn.entities.GiaoDichKhoaHoc;
import com.duantn.entities.GiaoDichKhoaHocChiTiet;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.HinhThucThanhToan;
import com.duantn.enums.TrangThaiGiaoDich;
import com.duantn.repositories.GiaoDichKhoaHocChiTietRepository;
import com.duantn.repositories.GiaoDichKhoaHocRepository;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.KhoaHocService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/hoc-vien")
public class ThanhToanController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private GiaoDichKhoaHocChiTietRepository giaoDichChiTietRepo;

    @Autowired
    private GiaoDichKhoaHocRepository giaoDichKhoaHocRepository;

    @Autowired
    private KhoaHocRepository khoaHocRepo;

    @GetMapping("/thanh-toan")
    public String hienThiThanhToan(@RequestParam("khoahocId") List<Integer> ids, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan tk = userDetails.getTaiKhoan();

        List<KhoaHoc> dsKhoaHoc = khoaHocService.findAllByIds(ids);

        BigDecimal tongTien = dsKhoaHoc.stream()
                .map(KhoaHoc::getGiagoc)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Lưu giao dịch sơ bộ
        GiaoDichKhoaHoc giaoDich = GiaoDichKhoaHoc.builder()
                .tongtien(tongTien)
                .tenhocvien(tk.getName())
                .taikhoan(tk)
                .trangthai(TrangThaiGiaoDich.CHO_XU_LY)
                .hinhThucThanhToan(HinhThucThanhToan.CHUYEN_KHOAN)
                .build();

        giaoDichKhoaHocRepository.save(giaoDich);

        // Truyền giaoDichId sang giao diện (nếu cần dùng lại)
        model.addAttribute("giaoDichId", giaoDich.getGiaodichId());

        List<Integer> khoaHocIds = dsKhoaHoc.stream()
                .map(KhoaHoc::getKhoahocId)
                .toList();

        model.addAttribute("khoaHocIds", khoaHocIds);
        model.addAttribute("dsKhoaHoc", dsKhoaHoc);
        model.addAttribute("tongTien", tongTien);

        return "views/gdienHocVien/thanh-toan";
    }

    @PostMapping("/api/thanh-toan/thanh-cong")
    @ResponseBody
    public ResponseEntity<?> luuGiaoDich(@RequestBody GiaoDichRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập");
        }

        TaiKhoan taiKhoan = userDetails.getTaiKhoan();

        try {
            // Tạo giao dịch chính
            // Tạo giao dịch chính
            GiaoDichKhoaHoc giaoDich = GiaoDichKhoaHoc.builder()
                    .tongtien(request.getTongTien())
                    .tenhocvien(taiKhoan.getName())
                    .taikhoan(taiKhoan)
                    .trangthai(TrangThaiGiaoDich.HOAN_THANH)
                    .hinhThucThanhToan(HinhThucThanhToan.CHUYEN_KHOAN)
                    .build();

            giaoDichKhoaHocRepository.save(giaoDich); // lưu để có ID

            // Tạo danh sách chi tiết (không dùng lambda trực tiếp)
            List<GiaoDichKhoaHocChiTiet> danhSachChiTiet = new ArrayList<>();

            for (Integer id : request.getKhoaHocIds()) {
                KhoaHoc khoaHoc = khoaHocRepo.findById(id).orElse(null);
                if (khoaHoc == null)
                    continue;

                GiaoDichKhoaHocChiTiet chiTiet = GiaoDichKhoaHocChiTiet.builder()
                        .giaoDichKhoaHoc(giaoDich)
                        .khoahoc(khoaHoc)
                        .dongia(khoaHoc.getGiaKhuyenMai())
                        .build();

                danhSachChiTiet.add(chiTiet);
            }

            giaoDichChiTietRepo.saveAll(danhSachChiTiet);

            return ResponseEntity.ok("Đã lưu giao dịch thành công");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Có lỗi xảy ra khi lưu giao dịch");
        }
    }

}
