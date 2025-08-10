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

import com.duantn.dtos.GiaoDichRequest;
import com.duantn.entities.DangHoc;
import com.duantn.entities.DoanhThuGiangVien;
import com.duantn.entities.GiaoDichKhoaHoc;
import com.duantn.entities.GiaoDichKhoaHocChiTiet;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.ThuNhapNenTang;
import com.duantn.enums.HinhThucThanhToan;
import com.duantn.enums.TrangThaiGiaoDich;
import com.duantn.repositories.DangHocRepository;
import com.duantn.repositories.DoanhThuGiangVienRepository;
import com.duantn.repositories.GiaoDichKhoaHocChiTietRepository;
import com.duantn.repositories.GiaoDichKhoaHocRepository;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.ThuNhapNenTangRepository;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.EmailThanhToanThanhCongService;
import com.duantn.services.KhoaHocService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private DangHocRepository dangHocRepository;

    @Autowired
    private KhoaHocRepository khoaHocRepo;

    @Autowired
    private DoanhThuGiangVienRepository doanhThuGiangVienRepository;

    @Autowired
    private EmailThanhToanThanhCongService emailThanhToanThanhCongService;

    @Autowired
    private ThuNhapNenTangRepository thuNhapNenTangRepository;

    @GetMapping("/thanh-toan")
    public String hienThiThanhToan(@RequestParam("khoahocId") List<Integer> ids, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan tk = userDetails.getTaiKhoan();

        List<KhoaHoc> dsKhoaHoc = khoaHocService.findAllByIds(ids);

        // ✅ Tính giá thực tế tại thời điểm thanh toán, KHÔNG ghi đè giagoc
        BigDecimal tongTien = BigDecimal.ZERO;
        Map<Integer, BigDecimal> giaHienTaiMap = new HashMap<>();

        for (KhoaHoc kh : dsKhoaHoc) {
            BigDecimal giaThucTe = khoaHocService.getGiaThanhToan(kh);
            giaHienTaiMap.put(kh.getKhoahocId(), giaThucTe);
            tongTien = tongTien.add(giaThucTe);
        }

        // ✅ Truyền Map này ra để hiển thị giá hiện tại
        model.addAttribute("giaHienTaiMap", giaHienTaiMap);

        GiaoDichKhoaHoc giaoDich = GiaoDichKhoaHoc.builder()
                .tongtien(tongTien)
                .tenhocvien(tk.getName())
                .taikhoan(tk)
                .trangthai(TrangThaiGiaoDich.CHO_XU_LY)
                .hinhThucThanhToan(HinhThucThanhToan.SmartBanking)
                .build();

        giaoDichKhoaHocRepository.save(giaoDich);

        model.addAttribute("giaoDichId", giaoDich.getGiaodichId());
        model.addAttribute("khoaHocIds", dsKhoaHoc.stream().map(KhoaHoc::getKhoahocId).toList());
        model.addAttribute("dsKhoaHoc", dsKhoaHoc);
        model.addAttribute("tongTien", tongTien);

        return "views/gdienHocVien/thanh-toan";
    }

    @PostMapping("/api/thanh-toan/thanh-cong")
    public ResponseEntity<?> xuLyThanhToanThanhCong(@RequestBody GiaoDichRequest request) {
        Integer giaoDichId = Integer.valueOf(request.getGiaoDichId());
        Optional<GiaoDichKhoaHoc> optionalGDKH = giaoDichKhoaHocRepository.findById(giaoDichId);

        if (optionalGDKH.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giao dịch");
        }

        GiaoDichKhoaHoc giaoDichKH = optionalGDKH.get();
        giaoDichKH.setTrangthai(TrangThaiGiaoDich.HOAN_THANH);
        giaoDichKhoaHocRepository.save(giaoDichKH);

        TaiKhoan taiKhoan = giaoDichKH.getTaikhoan();

        for (Integer khoaHocId : request.getKhoaHocIds()) {
            Optional<KhoaHoc> optionalKH = khoaHocRepo.findById(khoaHocId);
            if (optionalKH.isPresent()) {
                KhoaHoc kh = optionalKH.get();

                // ✅ Tính giá tại thời điểm hiện tại
                BigDecimal giaThucTe = khoaHocService.getGiaThanhToan(kh);

                GiaoDichKhoaHocChiTiet chiTiet = new GiaoDichKhoaHocChiTiet();
                chiTiet.setGiaoDichKhoaHoc(giaoDichKH);
                chiTiet.setKhoahoc(kh);
                chiTiet.setDongia(giaThucTe);
                giaoDichChiTietRepo.save(chiTiet);

                // ✅ Ghi vào bảng DangHoc
                DangHoc dangHoc = DangHoc.builder()
                        .taikhoan(taiKhoan)
                        .khoahoc(kh)
                        .dongia(giaThucTe)
                        .trangthai(false)
                        .daCap_ChungChi(false)
                        .build();
                dangHocRepository.save(dangHoc);

                // ✅ Ghi doanh thu giảng viên
                BigDecimal tiLeGV = BigDecimal.valueOf(0.7);
                BigDecimal tienNhanGV = giaThucTe.multiply(tiLeGV);

                DoanhThuGiangVien doanhThu = DoanhThuGiangVien.builder()
                        .sotiennhan(tienNhanGV)
                        .tenGiangVien(kh.getGiangVien().getTaikhoan().getName())
                        .taikhoanGV(kh.getGiangVien().getTaikhoan())
                        .dangHoc(dangHoc)
                        .build();
                doanhThuGiangVienRepository.save(doanhThu);

                // ✅ Ghi thu nhập nền tảng
                BigDecimal tiLeNenTang = BigDecimal.valueOf(0.3);
                BigDecimal tienNenTang = giaThucTe.multiply(tiLeNenTang);

                ThuNhapNenTang thuNhapNenTang = ThuNhapNenTang.builder()
                        .sotiennhan(tienNenTang)
                        .dangHoc(dangHoc)
                        .tenKhoaHoc(kh.getTenKhoaHoc())
                        .thuocGiangVien(kh.getGiangVien().getTaikhoan().getName())
                        .build();
                thuNhapNenTangRepository.save(thuNhapNenTang);
            }
        }

        // Gửi mail
        List<KhoaHoc> dsKhoaHoc = khoaHocService.findAllByIds(request.getKhoaHocIds());
        emailThanhToanThanhCongService.sendPaymentSuccessEmail(
                taiKhoan.getEmail(),
                taiKhoan.getName(),
                giaoDichKH.getGiaodichId().toString(),
                giaoDichKH.getTongtien().toPlainString(),
                dsKhoaHoc);

        return ResponseEntity.ok("Đã lưu chi tiết giao dịch thành công");
    }

    @PostMapping("/api/thanh-toan/that-bai")
    public ResponseEntity<?> xuLyThanhToanThatBai(@RequestBody GiaoDichRequest request) {
        Integer giaoDichId = Integer.valueOf(request.getGiaoDichId());
        Optional<GiaoDichKhoaHoc> optionalGDKH = giaoDichKhoaHocRepository.findById(giaoDichId);

        if (optionalGDKH.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy giao dịch");
        }

        GiaoDichKhoaHoc giaoDichKH = optionalGDKH.get();

        // Cập nhật trạng thái giao dịch thành THAT_BAI
        giaoDichKH.setTrangthai(TrangThaiGiaoDich.THAT_BAI);
        giaoDichKhoaHocRepository.save(giaoDichKH);

        return ResponseEntity.ok("Đã cập nhật trạng thái thất bại cho giao dịch");
    }

}