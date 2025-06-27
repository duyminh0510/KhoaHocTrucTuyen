package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hoc-vien")
public class HomeHocVienController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @GetMapping("")
    public String showHomeHocVien(Model model, Authentication authentication) {
        List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocList", khoaHocList);

        if (authentication != null && authentication.isAuthenticated()) {
            taiKhoanRepository.findByEmail(authentication.getName()).ifPresent(taiKhoan -> {
                Set<Integer> likedCourseIds = nguoiDungThichKhoaHocRepository.findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId())
                        .stream()
                        .map(like -> like.getKhoaHoc().getKhoahocId())
                        .collect(Collectors.toSet());
                model.addAttribute("likedCourseIds", likedCourseIds);
            });
        }
        if (!model.containsAttribute("likedCourseIds")) {
            model.addAttribute("likedCourseIds", Collections.emptySet());
        }
        
        return "views/gdienHocVien/home-hoc-vien";
    }

    @GetMapping("/khoa-hoc-yeu-thich")
    public String showKhoaHocYeuThich(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Integer currentUserId = taiKhoanRepository.findByEmail(authentication.getName())
                                                 .map(taiKhoan -> taiKhoan.getTaikhoanId())
                                                 .orElse(null);

        List<KhoaHoc> favoriteCourses = khoaHocService.findLikedCoursesByAccountId(currentUserId);
        model.addAttribute("favoriteCourses", favoriteCourses);
        return "views/gdienHocVien/khoa-hoc-yeu-thich";
    }

    @PostMapping("/khoa-hoc/{id}/like")
    @ResponseBody
    public ResponseEntity<?> likeCourse(@PathVariable Integer id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập.");
        }

        return taiKhoanRepository.findByEmail(authentication.getName())
            .map(taiKhoan -> {
                boolean isLiked = khoaHocService.toggleLike(id, taiKhoan.getTaikhoanId());
                KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
                
                if (khoaHoc != null) {
                    Map<String, Object> response = Map.of(
                        "newLikeCount", khoaHoc.getLuotThich() != null ? khoaHoc.getLuotThich() : 0,
                        "isLiked", isLiked
                    );
                    return ResponseEntity.ok(response);
                }
                return ResponseEntity.notFound().build();
            })
            .orElse(ResponseEntity.status(404).body("Không tìm thấy tài khoản."));
    }
} 