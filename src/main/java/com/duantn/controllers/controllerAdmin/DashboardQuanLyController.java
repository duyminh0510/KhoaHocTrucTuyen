package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.repositories.ThuNhapNenTangRepository;
import com.duantn.repositories.GiaoDichKhoaHocChiTietRepository;
import org.springframework.data.domain.PageRequest;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@Controller
@RequestMapping("/admin")
public class DashboardQuanLyController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private ThuNhapNenTangRepository thuNhapNenTangRepository;

    @Autowired
    private GiaoDichKhoaHocChiTietRepository giaoDichKhoaHocChiTietRepository;

    @GetMapping({"", "/"})
    public String home(Model model) {
        Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));
        Role hocVienRole = roleRepository.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền học viên"));

        List<TaiKhoan> giangViens = taiKhoanRepository.findByRole(giangVienRole);
        List<TaiKhoan> hocViens = taiKhoanRepository.findByRole(hocVienRole);
        List<KhoaHoc> khoaHocDangMo = khoaHocRepository.findByTrangThai(TrangThaiKhoaHoc.PUBLISHED);

        // Gọi tổng thu nhập tháng này
        BigDecimal tongThuNhap = thuNhapNenTangRepository.getTongThuNhap();
        model.addAttribute("tongThuNhap", tongThuNhap);

        // Tính tổng doanh thu quý này (3 tháng gần nhất)
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentQuarter = (currentMonth - 1) / 3 + 1;
        int startMonth = (currentQuarter - 1) * 3 + 1;
        LocalDateTime startOfQuarter = LocalDateTime.of(now.getYear(), startMonth, 1, 0, 0);
        BigDecimal doanhThuQuyNay = thuNhapNenTangRepository.getTongThuNhapTrongKhoang(startOfQuarter, now);
        model.addAttribute("doanhThuQuyNay", doanhThuQuyNay);

        model.addAttribute("soGiangVien", giangViens.size());
        model.addAttribute("soHocVien", hocViens.size());
        model.addAttribute("soKhoaHoc", khoaHocDangMo.size());
        model.addAttribute("tongThuNhap", tongThuNhap);

        // Lấy top 5 khóa học bán chạy nhất
        List<Integer> topKhoaHocIds = khoaHocRepository.findTopPurchasedCourseIds(PageRequest.of(0, 5));
        List<KhoaHoc> topKhoaHoc = topKhoaHocIds.isEmpty() ? List.of() : khoaHocRepository.findByIdInWithDetails(topKhoaHocIds);
        model.addAttribute("topKhoaHoc", topKhoaHoc);

        // Map số lượt mua cho từng khóa học
        Map<Integer, Long> topKhoaHocLuotMua = new HashMap<>();
        for (Integer khoaHocId : topKhoaHocIds) {
            long count = giaoDichKhoaHocChiTietRepository.countByKhoahoc_KhoahocId(khoaHocId);
            topKhoaHocLuotMua.put(khoaHocId, count);
        }
        model.addAttribute("topKhoaHocLuotMua", topKhoaHocLuotMua);

        return "views/gdienQuanly/home";
    }
}
