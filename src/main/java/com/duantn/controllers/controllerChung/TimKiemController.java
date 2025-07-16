package com.duantn.controllers.controllerChung;

import com.duantn.entities.KhoaHoc;
import com.duantn.services.KhoaHocService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TimKiemController {

    private final KhoaHocService khoaHocService;

    @GetMapping("/tim-kiem")
    public String timKiem(
            @RequestParam(value = "q", required = false) String query,
            Model model) {

        try {
            log.info("=== BẮT ĐẦU TÌM KIẾM KHÓA HỌC ===");
            log.info("Từ khóa: '{}'", query);

            List<KhoaHoc> ketQuaTimKiem = khoaHocService.timKiemTheoTenPublished(query);

            model.addAttribute("khoaHocList", khoaHocService.getTatCaKhoaHocPublished());
            model.addAttribute("ketQuaTimKiem", ketQuaTimKiem);
            model.addAttribute("soLuongKetQua", ketQuaTimKiem.size());
            model.addAttribute("query", query); // để hiển thị lại từ khóa

            log.info("Tìm thấy {} kết quả", ketQuaTimKiem.size());
            log.info("=== KẾT THÚC TÌM KIẾM ===");

            return "views/gdienChung/tim-kiem";

        } catch (Exception e) {
            log.error("❌ Lỗi khi tìm kiếm: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra khi tìm kiếm.");
            model.addAttribute("ketQuaTimKiem", List.of());
            model.addAttribute("soLuongKetQua", 0);
            model.addAttribute("khoaHocList", khoaHocService.getTatCaKhoaHocPublished());
            return "views/gdienChung/tim-kiem";
        }
    }

}
