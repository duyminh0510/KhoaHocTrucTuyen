package com.duantn.controllers.controllerChung;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class TrangChuController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @RequestMapping("/")
    public String home(HttpServletRequest request, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // Trường hợp chưa đăng nhập: Hiển thị trang chủ chung với danh sách khóa học
            List<KhoaHoc> allCourses = khoaHocService.getTatCaKhoaHoc(); // Lấy tất cả khóa học
            model.addAttribute("khoaHocList", allCourses); // Đặt vào model với tên "khoaHocList"
            model.addAttribute("likedCourseIds", Collections.emptySet()); // Không có user, nên
                                                                          // không có khóa học yêu
                                                                          // thích

            // Bạn có thể thêm các khóa học mới nhất, khóa học nổi bật ở đây nếu cần cho
            // trang chung
            // model.addAttribute("newCourses", khoaHocService.getNewestCourses(8));
            // model.addAttribute("topCourses", khoaHocService.getTopPurchasedCourses(8));

            return "views/gdienChung/home"; // Trả về trang chủ chung
        }

        // Trường hợp đã đăng nhập: Tùy vai trò chuyển trang
        boolean isHocVien = request.isUserInRole("ROLE_HOCVIEN");
        boolean isGiangVien = request.isUserInRole("ROLE_GIANGVIEN");

        if (isHocVien) {
            // Trang chủ cho học viên
            List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc(); // Lấy tất cả khóa học
            model.addAttribute("khoaHocList", khoaHocList); // Đặt vào model với tên "khoaHocList"

            // Lấy danh sách các khóa học đã thích của học viên
            taiKhoanRepository.findByEmail(authentication.getName()).ifPresent(taiKhoan -> {
                Set<Integer> likedCourseIds = nguoiDungThichKhoaHocRepository
                        .findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId()).stream()
                        .map(like -> like.getKhoaHoc().getKhoahocId()).collect(Collectors.toSet());
                model.addAttribute("likedCourseIds", likedCourseIds);
            });

            if (!model.containsAttribute("likedCourseIds")) {
                model.addAttribute("likedCourseIds", Collections.emptySet());
            }

            return "views/gdienHocVien/home"; // Trả về trang chủ học viên
        }

        if (isGiangVien) {
            return "views/gdienGiangVien/home"; // Trả về trang chủ giảng viên
        }

        // Nếu không phải học viên hay giảng viên
        return "redirect:/auth/login?error=unauthorized";
    }
}