package com.duantn.controllers.controllerGiangVien;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.GiangVienService;
import com.duantn.entities.GiangVien;
import com.duantn.dtos.DoanhThuKhoaHocGiangVienDto;
import java.util.List;

@Controller
public class ThongKeController {

    @Autowired
    private GiangVienService giangVienService;

    @GetMapping("/giangvien/danh-sach-thong-ke")
    public String showThongKe(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        GiangVien giangVien = giangVienService.findByTaikhoan(userDetails.getTaiKhoan());
        if (giangVien == null) {
            return "redirect:/giangvien/trang-giang-vien";
        }

        Integer giangVienId = giangVien.getGiangvienId();
        List<DoanhThuKhoaHocGiangVienDto> doanhThuKhoaHoc = giangVienService.thongKeDoanhThuTheoGiangVien(giangVienId);

        // Tổng doanh thu
        double tongDoanhThu = doanhThuKhoaHoc.stream()
                .mapToDouble(DoanhThuKhoaHocGiangVienDto::getTongTienNhan)
                .sum();

        // Tổng học viên đăng ký
        int tongHocVien = (int) doanhThuKhoaHoc.stream()
                .mapToLong(DoanhThuKhoaHocGiangVienDto::getSoLuotDangKy)
                .sum();

        // Tổng số khóa học đang dạy
        int soKhoaHoc = doanhThuKhoaHoc.size();

        // Trung bình điểm đánh giá (giả sử có hàm lấy điểm đánh giá)
        double diemTrungBinh = giangVienService.tinhDiemDanhGiaTrungBinh(giangVienId); // cần triển khai thêm trong
                                                                                       // service

        // Doanh thu tháng này (giả lập hoặc thêm hàm riêng)
        double doanhThuThang = giangVienService.tinhDoanhThuThangNay(giangVienId); // cần triển khai thêm trong service

        model.addAttribute("doanhThuKhoaHoc", doanhThuKhoaHoc);
        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("tongHocVien", tongHocVien);
        model.addAttribute("soKhoaHoc", soKhoaHoc);
        model.addAttribute("diemTrungBinh", diemTrungBinh);
        model.addAttribute("doanhThuThang", doanhThuThang);

        return "views/gdienGiangVien/thong-ke";
    }

}
