package com.duantn.controllers.controllerGiangVien;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.dtos.DoanhThuKhoaHocGiangVienDto;
import com.duantn.dtos.HocVienTheoKhoaHocDto;
import com.duantn.dtos.KhoaHocDiemDto;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.DoanhThuGiangVienService;
import com.duantn.services.GiangVienService;

@Controller
@RequestMapping("/giangvien")
public class ThongKeController {

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private DoanhThuGiangVienService doanhThuGiangVienService;

    @RequestMapping("/danh-sach-thong-ke")
    public String showThongKe(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        GiangVien giangVien = giangVienService.findByTaikhoan(userDetails.getTaiKhoan());
        if (giangVien == null) {
            return "redirect:/auth/dangnhap";
        }

        Integer giangVienId = giangVien.getGiangvienId();
        List<DoanhThuKhoaHocGiangVienDto> doanhThuKhoaHoc =
                giangVienService.thongKeDoanhThuTheoGiangVien(giangVienId);

        double tongTienNhan = giangVienService.layTongTienNhan(giangVien.getGiangvienId());

        long tongHocVien = giangVienService.demHocVienTheoGiangVien(giangVienId);

        int soKhoaHoc = doanhThuKhoaHoc.size();

        double diemTrungBinh = giangVienService.tinhDiemDanhGiaTrungBinh(giangVienId);

        YearMonth thangHienTai = YearMonth.now();
        LocalDateTime startDate = thangHienTai.atDay(1).atStartOfDay();
        LocalDateTime endDate = thangHienTai.atEndOfMonth().atTime(23, 59, 59);

        BigDecimal doanhThuThang = doanhThuGiangVienService.tinhDoanhThuTheoKhoangNgay(giangVienId,
                startDate, endDate);

        model.addAttribute("doanhThuKhoaHoc", doanhThuKhoaHoc);
        model.addAttribute("tongTienNhan", tongTienNhan);
        model.addAttribute("tongHocVien", tongHocVien);
        model.addAttribute("soKhoaHoc", soKhoaHoc);
        model.addAttribute("diemTrungBinh", diemTrungBinh);
        model.addAttribute("doanhThuThang", doanhThuThang);

        return "views/gdienGiangVien/thong-ke";
    }

    @GetMapping("/thong-ke-hoc-vien")
    public String thongKeHocVienTheoKhoaHoc(@AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        GiangVien giangVien = giangVienService.findByTaikhoan(userDetails.getTaiKhoan());
        if (giangVien == null) {
            return "redirect:/giangvien/trang-giang-vien";
        }

        Integer giangVienId = giangVien.getGiangvienId();
        List<HocVienTheoKhoaHocDto> hocVienList =
                giangVienService.thongKeHocVienTheoKhoaHoc(giangVienId);

        model.addAttribute("hocVienList", hocVienList);
        return "views/gdienGiangVien/thong-ke-chi-tiet-hvien";
    }

    @GetMapping("/khoa-hoc-dang-day")
    public String danhSachKhoaHocDangDay(@AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        GiangVien gv = giangVienService.findByTaikhoan(userDetails.getTaiKhoan());
        if (gv == null)
            return "redirect:/giangvien/trang-giang-vien";

        List<KhoaHoc> khoaHocDangDay = giangVienService.timTatCaKhoaHocDangDay(gv.getGiangvienId());
        model.addAttribute("danhSachKhoaHoc", khoaHocDangDay);

        return "views/gdienGiangVien/thong-ke-khoa-hoc-dang-day";
    }

    @GetMapping("/danh-gia-trung-binh")
    public String danhGiaTrungBinhTheoKhoaHoc(
            @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        GiangVien gv = giangVienService.findByTaikhoan(userDetails.getTaiKhoan());
        if (gv == null)
            return "redirect:/giangvien/trang-giang-vien";

        List<KhoaHocDiemDto> danhGiaList =
                giangVienService.layDiemTrungBinhCacKhoaHocXuatBan(gv.getGiangvienId());
        model.addAttribute("danhGiaList", danhGiaList);
        return "views/gdienGiangVien/thong-ke-danh-gia-trung-binh";
    }
}
