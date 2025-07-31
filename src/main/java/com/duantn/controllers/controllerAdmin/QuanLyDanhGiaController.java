package com.duantn.controllers.controllerAdmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.BinhLuan;
import com.duantn.entities.DanhGia;
import com.duantn.entities.GiangVien;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.BinhLuanRepository;
import com.duantn.repositories.DanhGiaRepository;
import com.duantn.repositories.GiangVienRepository;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.services.DanhGiaService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/quanly-danh-gia-binh-luan")
@RequiredArgsConstructor
public class QuanLyDanhGiaController {

    private final DanhGiaRepository danhGiaRepo;
    private final BinhLuanRepository binhLuanRepo;
    private final KhoaHocRepository khoaHocRepo;
    private final GiangVienRepository giangVienRepo;
    private final DanhGiaService danhGiaService;

    @GetMapping
    public String showFeedbackPage(
            @RequestParam(value = "courseId", required = false) Integer courseId,
            @RequestParam(value = "rating", required = false) Integer rating,
            @RequestParam(value = "lecturer", required = false) Integer lecturerId,
            @RequestParam(value = "sort", required = false, defaultValue = "newest") String sort,
            @RequestParam(value = "sortComment", required = false, defaultValue = "desc") String sortComment,
            Model model) {

        List<KhoaHoc> courses = khoaHocRepo.findAll();
        List<GiangVien> lecturers = giangVienRepo.findAll();
        List<DanhGia> feedbackList = danhGiaRepo.findAll();
        List<BinhLuan> commentList = binhLuanRepo.findAll();

        if (courseId != null) {
            feedbackList = feedbackList.stream()
                    .filter(f -> f.getKhoahoc().getKhoahocId().equals(courseId))
                    .collect(Collectors.toList());

            commentList = commentList.stream()
                    .filter(c -> c.getBaiGiang() != null && c.getBaiGiang().getChuong() != null &&
                            c.getBaiGiang().getChuong().getKhoahoc().getKhoahocId().equals(courseId))
                    .collect(Collectors.toList());
        }

        if (lecturerId != null) {
            feedbackList = feedbackList.stream()
                    .filter(f -> f.getKhoahoc().getGiangVien() != null &&
                            f.getKhoahoc().getGiangVien().getTaikhoan().getTaikhoanId().equals(lecturerId))
                    .collect(Collectors.toList());

            commentList = commentList.stream()
                    .filter(c -> {
                        if (c.getBaiGiang() != null && c.getBaiGiang().getChuong() != null) {
                            KhoaHoc kh = c.getBaiGiang().getChuong().getKhoahoc();
                            return kh.getGiangVien() != null &&
                                    kh.getGiangVien().getTaikhoan().getTaikhoanId().equals(lecturerId);
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        if (rating != null) {
            feedbackList = feedbackList.stream()
                    .filter(f -> f.getDiemDanhGia().equals(rating))
                    .collect(Collectors.toList());
        }

        // Sort đánh giá
        if ("oldest".equals(sort)) {
            feedbackList.sort(Comparator.comparing(DanhGia::getNgayDanhGia));
        } else {
            feedbackList.sort(Comparator.comparing(DanhGia::getNgayDanhGia).reversed());
        }

        // Sort bình luận
        if ("asc".equals(sortComment)) {
            commentList.sort(Comparator.comparing(BinhLuan::getNgayBinhLuan));
        } else {
            commentList.sort(Comparator.comparing(BinhLuan::getNgayBinhLuan).reversed());
        }

        double avgRating = feedbackList.stream()
                .mapToInt(DanhGia::getDiemDanhGia)
                .average()
                .orElse(0);

        Map<Integer, Long> ratingMap = feedbackList.stream()
                .collect(Collectors.groupingBy(DanhGia::getDiemDanhGia, Collectors.counting()));

        List<Long> chartData = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            chartData.add(ratingMap.getOrDefault(i, 0L));
        }

        model.addAttribute("courses", courses);
        model.addAttribute("lecturers", lecturers);
        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("commentList", commentList);

        model.addAttribute("avgRating", avgRating);
        model.addAttribute("totalReviews", feedbackList.size());
        model.addAttribute("chartData", chartData);

        model.addAttribute("selectedCourseId", courseId);
        model.addAttribute("selectedRating", rating);
        model.addAttribute("selectedLecturer", lecturerId);
        model.addAttribute("selectedSort", sort);
        model.addAttribute("selectedSortComment", sortComment);

        return "views/gdienQuanLy/quanly-danh-gia";
    }

    @GetMapping("/api/danh-gia")
    @ResponseBody
    public List<Map<String, Object>> getFeedbackAjax(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer lecturer,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "newest") String sort) {

        List<DanhGia> list = danhGiaRepo.findAll().stream()
                .filter(f -> courseId == null || f.getKhoahoc().getKhoahocId().equals(courseId))
                .filter(f -> lecturer == null
                        || f.getKhoahoc().getGiangVien().getTaikhoan().getTaikhoanId().equals(lecturer))
                .filter(f -> rating == null || f.getDiemDanhGia() == rating)
                .sorted((a, b) -> {
                    if ("oldest".equals(sort)) {
                        return a.getNgayDanhGia().compareTo(b.getNgayDanhGia());
                    } else {
                        return b.getNgayDanhGia().compareTo(a.getNgayDanhGia());
                    }
                })
                .collect(Collectors.toList());

        return list.stream().map(f -> {
            Map<String, Object> map = new HashMap<>();
            map.put("danhgiaId", f.getDanhgiaId());
            map.put("diemDanhGia", f.getDiemDanhGia());
            map.put("noiDung", f.getNoiDung());
            map.put("ngayDanhGia", f.getNgayDanhGia().toString());
            map.put("taikhoan", Map.of("email", f.getTaikhoan().getEmail()));
            map.put("khoahoc", Map.of(
                    "tenKhoaHoc", f.getKhoahoc().getTenKhoaHoc(),
                    "giangVien",
                    Map.of("taikhoan", Map.of("name", f.getKhoahoc().getGiangVien().getTaikhoan().getName()))));
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/api/binh-luan")
    @ResponseBody
    public List<Map<String, Object>> getCommentsAjax(
            @RequestParam(value = "courseId", required = false) Integer courseId,
            @RequestParam(value = "sortComment", defaultValue = "desc") String sort) {

        List<BinhLuan> list = binhLuanRepo.findAll().stream()
                .filter(c -> c.getBaiGiang() != null &&
                        c.getBaiGiang().getChuong() != null &&
                        c.getBaiGiang().getChuong().getKhoahoc() != null &&
                        (courseId == null ||
                                c.getBaiGiang().getChuong().getKhoahoc().getKhoahocId().equals(courseId)))
                .sorted((a, b) -> {
                    if ("asc".equals(sort)) {
                        return a.getNgayBinhLuan().compareTo(b.getNgayBinhLuan());
                    } else {
                        return b.getNgayBinhLuan().compareTo(a.getNgayBinhLuan());
                    }
                })
                .collect(Collectors.toList());

        return list.stream().map(c -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", c.getBinhluanId());
            map.put("email", c.getTaikhoan().getEmail());
            map.put("content", c.getNoiDung());
            map.put("date", c.getNgayBinhLuan().toString());
            map.put("courseName", c.getBaiGiang().getChuong().getKhoahoc().getTenKhoaHoc());
            return map;
        }).collect(Collectors.toList());
    }

    @DeleteMapping("/api/binh-luan/{id}")
    @ResponseBody
    public String deleteCommentAjax(@PathVariable("id") Integer id) {
        binhLuanRepo.deleteById(id);
        return "deleted";
    }

    @GetMapping(value = "/api/chi-tiet-danh-gia/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getFeedbackDetail(@PathVariable("id") Integer id) {
        DanhGia fb = danhGiaService.findById(id).orElse(null);
        if (fb == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> data = new HashMap<>();
        data.put("avatar", fb.getKhoahoc().getAnhBia()); // hoặc getAvatar()
        data.put("khoaHoc", fb.getKhoahoc().getTenKhoaHoc());
        data.put("avatarHocVien", fb.getTaikhoan().getAvatar()); // ✅ avatar học viên
        data.put("giangVien", fb.getKhoahoc().getGiangVien().getTaikhoan().getName());
        data.put("hocVien", fb.getTaikhoan().getName());
        data.put("sao", fb.getDiemDanhGia());
        data.put("noiDung", fb.getNoiDung());
        data.put("ngay", fb.getNgayDanhGia());

        return ResponseEntity.ok(data);
    }

}