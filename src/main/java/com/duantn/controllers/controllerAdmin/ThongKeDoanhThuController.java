package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duantn.repositories.ThuNhapNenTangRepository;

@Controller
public class ThongKeDoanhThuController {

    @Autowired
    private ThuNhapNenTangRepository thuNhapRepo;

    @GetMapping("/admin/doanhthu")
    public String hienThiDoanhThu(
            @RequestParam(name = "start", required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end",
                    required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            Model model) {

        // Tổng toàn thời gian
        BigDecimal tongDoanhThu = thuNhapRepo.getTongThuNhap();

        // Tổng doanh thu tháng hiện tại
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDateTime.now();
        BigDecimal doanhThuThang = thuNhapRepo.getTongThuNhapTrongKhoang(startOfMonth, endOfMonth);

        // Doanh thu trong khoảng nếu có chọn lọc
        BigDecimal doanhThuLoc = null;
        if (start != null && end != null) {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
            doanhThuLoc = thuNhapRepo.getTongThuNhapTrongKhoang(startDateTime, endDateTime);
        }

        model.addAttribute("doanhThu", tongDoanhThu);
        model.addAttribute("doanhThuThang", doanhThuThang);
        model.addAttribute("doanhThuLoc", doanhThuLoc);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        return "views/gdienQuanLy/doanhthu";
    }
}
