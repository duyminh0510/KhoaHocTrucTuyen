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
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TimKiemController {

    private final KhoaHocService khoaHocService;

    @GetMapping("/tim-kiem")
    public String timKiem(@RequestParam(value = "q", required = false) String query, Model model) {
        try {
            log.info("=== BẮT ĐẦU TÌM KIẾM ===");
            log.info("Từ khóa tìm kiếm: '{}'", query);

            List<KhoaHoc> ketQuaTimKiem;
            if (query != null && !query.trim().isEmpty()) {
                String trimmedQuery = query.trim();
                ketQuaTimKiem = khoaHocService.timKiemTheoTenIgnoreCase(trimmedQuery);
                model.addAttribute("query", trimmedQuery);
                log.info("Kết quả tìm kiếm: {} khóa học", ketQuaTimKiem.size());
                for (KhoaHoc kh : ketQuaTimKiem) {
                    log.info("Tìm thấy: ID={}, Tên='{}', Trạng thái={}", kh.getKhoahocId(), kh.getTenKhoaHoc(),
                            kh.getTrangThai());
                }
                if (ketQuaTimKiem.isEmpty()) {
                    List<KhoaHoc> goiYKhoaHoc = khoaHocService.layKhoaHocDeXuat(4);
                    model.addAttribute("goiYKhoaHoc", goiYKhoaHoc);
                }
            } else {
                ketQuaTimKiem = khoaHocService.layTatCaKhoaHoc();
                model.addAttribute("query", "");
            }
            model.addAttribute("ketQuaTimKiem", ketQuaTimKiem);
            model.addAttribute("soLuongKetQua", ketQuaTimKiem.size());

            log.info("=== KẾT THÚC TÌM KIẾM ===");

            return "views/gdienChung/tim-kiem";
        } catch (Exception e) {
            log.error("❌ Lỗi khi tìm kiếm khóa học: {}", e.getMessage(), e);
            model.addAttribute("error", "Có lỗi xảy ra khi tìm kiếm. Vui lòng thử lại.");
            model.addAttribute("ketQuaTimKiem", List.of());
            model.addAttribute("soLuongKetQua", 0);
            return "views/gdienChung/tim-kiem";
        }
    }

    // Endpoint test để kiểm tra tìm kiếm
    @GetMapping("/test-tim-kiem")
    public String testTimKiem(Model model) {
        log.info("=== TEST TÌM KIẾM ===");

        // Test tìm kiếm với từ khóa "Java"
        List<KhoaHoc> ketQuaJava = khoaHocService.timKiemTheoTenIgnoreCase("Java");
        log.info("Tìm 'Java': {} kết quả", ketQuaJava.size());

        // Test tìm kiếm với từ khóa "React"
        List<KhoaHoc> ketQuaReact = khoaHocService.timKiemTheoTenIgnoreCase("React");
        log.info("Tìm 'React': {} kết quả", ketQuaReact.size());

        // Test tìm kiếm với từ khóa "Python"
        List<KhoaHoc> ketQuaPython = khoaHocService.timKiemTheoTenIgnoreCase("Python");
        log.info("Tìm 'Python': {} kết quả", ketQuaPython.size());

        // Test tìm kiếm với từ khóa "JavaScript"
        List<KhoaHoc> ketQuaJS = khoaHocService.timKiemTheoTenIgnoreCase("JavaScript");
        log.info("Tìm 'JavaScript': {} kết quả", ketQuaJS.size());

        model.addAttribute("testResults", Map.of(
                "Java", ketQuaJava.size(),
                "React", ketQuaReact.size(),
                "Python", ketQuaPython.size(),
                "JavaScript", ketQuaJS.size()));

        return "views/gdienChung/tim-kiem";
    }
}