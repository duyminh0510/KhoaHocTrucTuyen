package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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
            @RequestParam(name = "quy", required = false) Integer quy, Model model) {

        // Tổng toàn bộ
        BigDecimal tongDoanhThu = thuNhapRepo.getTongThuNhap();

        // Doanh thu tháng hiện tại
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDateTime.now();
        BigDecimal doanhThuThang = thuNhapRepo.getTongThuNhapTrongKhoang(startOfMonth, endOfMonth);

        // Doanh thu theo khoảng ngày lọc
        BigDecimal doanhThuLoc = null;
        if (start != null && end != null) {
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);
            doanhThuLoc = thuNhapRepo.getTongThuNhapTrongKhoang(startDateTime, endDateTime);
            model.addAttribute("start", start);
            model.addAttribute("end", end);
        }

        // Doanh thu theo quý
        BigDecimal doanhThuQuy = null;
        if (quy != null && quy >= 1 && quy <= 4) {
            int year = LocalDate.now().getYear();
            int startMonthValue = (quy - 1) * 3 + 1;
            int endMonthValue = startMonthValue + 2;

            LocalDateTime startQuy = LocalDate.of(year, startMonthValue, 1).atStartOfDay();
            LocalDateTime endQuy =
                    YearMonth.of(year, endMonthValue).atEndOfMonth().atTime(LocalTime.MAX);

            doanhThuQuy = thuNhapRepo.getTongThuNhapTrongKhoang(startQuy, endQuy);
            model.addAttribute("startQuy", startQuy.toLocalDate());
            model.addAttribute("endQuy", endQuy.toLocalDate());
            model.addAttribute("quy", quy);
        }

        model.addAttribute("doanhThu", tongDoanhThu);
        model.addAttribute("doanhThuThang", doanhThuThang);
        model.addAttribute("doanhThuLoc", doanhThuLoc);
        model.addAttribute("doanhThuQuy", doanhThuQuy);

        return "views/gdienQuanLy/doanhthu";
    }
}
