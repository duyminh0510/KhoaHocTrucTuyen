package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.ThuNhapNenTangRepository;
import com.duantn.services.ThongKeService;

@Controller
public class DashboardQuanLyController {
    @Autowired
    private ThongKeService thongKeService;
    @Autowired
    private ThuNhapNenTangRepository thuNhapRepo;

    @GetMapping("/admin")
    public String adminHome(Model model) {
        model.addAttribute("prefix", "admin");
        return loadDashboard(model);
    }

    @GetMapping("/nhanvien")
    public String nhanvienHome(Model model) {
        model.addAttribute("prefix", "nhanvien");
        return loadDashboard(model);
    }

    private String loadDashboard(Model model) {
        List<String> labels = thongKeService.getTopKhoaHocLabels();
        List<Long> soLuong = thongKeService.getTopKhoaHocSoLuong();

        Map<String, Long> pieData = thongKeService.thongKeTaiKhoanTheoVaiTro();
        Map<String, BigDecimal> map = thongKeService.getDoanhThuTheo6ThangGanNhat();

        List<String> doanhThuLabels = new ArrayList<>(map.keySet());
        List<BigDecimal> data = new ArrayList<>(map.values());

        List<Object[]> topDanhMuc = thongKeService.getTop5DanhMuc();

        List<String> labelsDanhMuc = new ArrayList<>();
        List<Long> values = new ArrayList<>();

        for (Object[] row : topDanhMuc) {
            labelsDanhMuc.add((String) row[0]);
            values.add((Long) row[1]);
        }

        int tongDaXuatBan = thongKeService.countKhoaHocByTrangThai(TrangThaiKhoaHoc.PUBLISHED);
        BigDecimal tongDoanhThu = thuNhapRepo.getTongThuNhap();
        int tongHocVienDangKy = thongKeService.countHocVienDaDangKy();

        model.addAttribute("tongDoanhThu", tongDoanhThu);
        model.addAttribute("topKhoaHocLabels", labels);
        model.addAttribute("topKhoaHocSoLuong", soLuong);
        model.addAttribute("roleLabels", pieData.keySet());
        model.addAttribute("roleCounts", pieData.values());
        model.addAttribute("doanhThuLabels", doanhThuLabels);
        model.addAttribute("doanhThuData", data);
        model.addAttribute("labelsDanhMuc", labelsDanhMuc);
        model.addAttribute("values", values);
        model.addAttribute("tongDaXuatBan", tongDaXuatBan);
        model.addAttribute("soGiangVien", thongKeService.tongGiangVien());
        model.addAttribute("tongHocVienDangKy", tongHocVienDangKy);

        return "views/gdienQuanly/home";
    }
}
