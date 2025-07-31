package com.duantn.controllers.controllerGiangVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.BaiGiang;
import com.duantn.entities.GiangVien;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.BaiGiangRepository;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;
import com.duantn.enums.TrangThaiKhoaHoc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class DashboardGiangVienController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private GiangVienRepository giangVienRepository;

    @Autowired
    private BaiGiangRepository baiGiangRepository;

    @GetMapping("/giangvien/trang-giang-vien")
public String homegiangvien(
    @RequestParam(name = "keyword", required = false) String keyword,
    Model model) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
    if (taiKhoan == null) return "redirect:/dang-nhap";

    GiangVien giangVien = giangVienRepository.findByTaikhoan_TaikhoanId(taiKhoan.getTaikhoanId()).orElse(null);
    if (giangVien == null) return "redirect:/dang-ky-giang-vien";

    List<KhoaHoc> khoaHocList;

    if (keyword != null && !keyword.trim().isEmpty()) {
        khoaHocList = khoaHocRepository
            .findByGiangVien_GiangvienIdAndTenKhoaHocContainingIgnoreCaseAndTrangThaiIn(
                giangVien.getGiangvienId(),
                keyword.trim(),
                List.of(
                    TrangThaiKhoaHoc.PUBLISHED,
                    TrangThaiKhoaHoc.DRAFT,
                    TrangThaiKhoaHoc.REJECTED,
                    TrangThaiKhoaHoc.PENDING_APPROVAL
                ));
    } else {
        khoaHocList = khoaHocRepository
            .findByGiangVien_GiangvienIdAndTrangThaiIn(
                giangVien.getGiangvienId(),
                List.of(
                    TrangThaiKhoaHoc.PUBLISHED,
                    TrangThaiKhoaHoc.DRAFT,
                    TrangThaiKhoaHoc.REJECTED,
                    TrangThaiKhoaHoc.PENDING_APPROVAL
                ));
    }

    model.addAttribute("khoaHocList", khoaHocList);
    model.addAttribute("keyword", keyword);

    return "views/gdienGiangVien/home";
}


    @GetMapping("/giangvien/khoa-hoc/chi-tiet")
public String chiTietKhoaHocGiangVien(@RequestParam("id") Integer id, Model model) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
    if (taiKhoan == null) return "redirect:/dang-nhap";

    GiangVien giangVien = giangVienRepository.findByTaikhoan_TaikhoanId(taiKhoan.getTaikhoanId()).orElse(null);
    if (giangVien == null) return "redirect:/dang-ky-giang-vien";

    // Lấy khoá học đầy đủ thông tin (kèm chương + bài giảng)
    KhoaHoc khoaHoc = khoaHocRepository.findByIdWithDetails(id).orElse(null);
    if (khoaHoc == null || khoaHoc.getGiangVien() == null ||
        !khoaHoc.getGiangVien().getGiangvienId().equals(giangVien.getGiangvienId())) {
        return "redirect:/giangvien/trang-giang-vien";
    }

    model.addAttribute("khoaHoc", khoaHoc);
    model.addAttribute("chuongs", khoaHoc.getChuongs());

    // Kiểm tra và lấy bài giảng đầu tiên nếu có
    Integer baiGiangId = null;
    boolean coNoiDung = false;

    if (khoaHoc.getChuongs() != null) {
        for (var chuong : khoaHoc.getChuongs()) {
            if (chuong.getBaiGiangs() != null && !chuong.getBaiGiangs().isEmpty()) {
                BaiGiang baiDauTien = chuong.getBaiGiangs().get(0);
                baiGiangId = baiDauTien.getBaiGiangId(); // ✅ đúng tên getter
                coNoiDung = true;
                break;
            }
        }
    }

    model.addAttribute("coNoiDung", coNoiDung);
    model.addAttribute("baiGiangId", baiGiangId);

    return "views/gdienGiangVien/khoa-hoc-chi-tiet-da-tao";
}


    @GetMapping("/giangvien/khoa-hoc/noi-dung-bai-giang")
    public String xemNoiDungBaiGiang(@RequestParam("id") Integer baiGiangId, Model model) {
    BaiGiang baiGiang = baiGiangRepository.findByIdWithDetails(baiGiangId);

    // Kiểm tra null đầy đủ
    if (baiGiang == null || baiGiang.getChuong() == null || baiGiang.getChuong().getKhoahoc() == null) {
        return "redirect:/giangvien/trang-giang-vien";
    }

    KhoaHoc khoaHoc = baiGiang.getChuong().getKhoahoc(); // dùng đúng tên field

    model.addAttribute("baiGiang", baiGiang);
    model.addAttribute("khoaHoc", khoaHoc);
    model.addAttribute("chuongs", khoaHoc.getChuongs()); // dùng đúng tên getter
    model.addAttribute("chuongDangMoId", baiGiang.getChuong().getChuongId());
    model.addAttribute("baiGiangDangHocId", baiGiangId);

    // Bổ sung các biến mặc định an toàn cho Thymeleaf
    model.addAttribute("baiViet", null);
    model.addAttribute("thuTuBaiTracNghiem", 0);
    model.addAttribute("tongSoCauHoi", 0);

    return "views/gdienGiangVien/xem-noi-dung-chi-tiet";
}

    @PostMapping("/giangvien/khoa-hoc/xoa/{id}")
public String xoaKhoaHoc(
        @PathVariable("id") Integer khoaHocId,
        RedirectAttributes redirectAttributes) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);
    if (taiKhoan == null) return "redirect:/dang-nhap";

    GiangVien giangVien = giangVienRepository.findByTaikhoan_TaikhoanId(taiKhoan.getTaikhoanId()).orElse(null);
    if (giangVien == null) return "redirect:/dang-ky-giang-vien";

    boolean daXoa = khoaHocRepository
        .findById(khoaHocId)
        .filter(kh -> kh.getGiangVien().getGiangvienId().equals(giangVien.getGiangvienId()))
        .map(kh -> khoaHocService.xoaKhoaHocNeuKhongCoNguoiHoc(khoaHocId))
        .orElse(false);

    if (daXoa) {
        redirectAttributes.addFlashAttribute("message", "Đã xoá khoá học thành công.");
    } else {
        redirectAttributes.addFlashAttribute("message", "Khoá học đã có người học. Hệ thống đã ẩn khoá học.");
    }

    return "redirect:/giangvien/trang-giang-vien";
}


}
