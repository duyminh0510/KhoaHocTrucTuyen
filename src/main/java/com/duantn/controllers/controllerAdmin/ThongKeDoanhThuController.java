package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duantn.entities.ThuNhapNenTang;
import com.duantn.repositories.ThuNhapNenTangRepository;

@Controller
public class ThongKeDoanhThuController {

    @Autowired
    private ThuNhapNenTangRepository thuNhapRepo;

    @GetMapping("/admin/doanhthu")
    public String hienThiDoanhThu(
            @RequestParam(name = "start", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(name = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(name = "quy", required = false) Integer quy,
            @RequestParam(name = "nam", required = false) Integer nam,
            @RequestParam(name = "page", defaultValue = "0") int page, Model model) {

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);

        // Tổng doanh thu
        BigDecimal tongDoanhThu = thuNhapRepo.getTongThuNhap();

        // Doanh thu tháng hiện tại
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfMonth = LocalDateTime.now();
        BigDecimal doanhThuThang = thuNhapRepo.getTongThuNhapTrongKhoang(startOfMonth, endOfMonth);

        BigDecimal doanhThuQuy = null;
        Page<ThuNhapNenTang> chiTietPage;

        // Nếu có quý và có năm
        if (quy != null && quy >= 1 && quy <= 4 && nam != null) {
            int startMonth = (quy - 1) * 3 + 1;
            int endMonth = startMonth + 2;

            LocalDateTime startQuy = LocalDate.of(nam, startMonth, 1).atStartOfDay();
            LocalDateTime endQuy = YearMonth.of(nam, endMonth).atEndOfMonth().atTime(LocalTime.MAX);

            doanhThuQuy = thuNhapRepo.getTongThuNhapTrongKhoang(startQuy, endQuy);
            chiTietPage = thuNhapRepo.findByNgaynhanBetween(startQuy, endQuy, pageable);

            model.addAttribute("quy", quy);
            model.addAttribute("nam", nam);

        } else if (start != null && end != null) {
            // Nếu có lọc theo khoảng thời gian
            LocalDateTime startDateTime = start.atStartOfDay();
            LocalDateTime endDateTime = end.atTime(LocalTime.MAX);

            doanhThuQuy = thuNhapRepo.getTongThuNhapTrongKhoang(startDateTime, endDateTime);
            chiTietPage = thuNhapRepo.findByNgaynhanBetween(startDateTime, endDateTime, pageable);

            model.addAttribute("start", start);
            model.addAttribute("end", end);
        } else {
            // Không có lọc, lấy tất cả
            doanhThuQuy = null;
            chiTietPage = thuNhapRepo.findAll(pageable);
        }

        model.addAttribute("doanhThu", tongDoanhThu);
        model.addAttribute("doanhThuThang", doanhThuThang);
        model.addAttribute("doanhThuQuy", doanhThuQuy);
        model.addAttribute("chiTietDoanhThu", chiTietPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", chiTietPage.getTotalPages());

        return "views/gdienQuanLy/doanhthu";
    }
}