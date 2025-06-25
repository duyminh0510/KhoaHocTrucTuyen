package com.duantn.controllers.controllerKhoaHoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/khoa-hoc")
@RequiredArgsConstructor
public class CourseController {

    private final KhoaHocRepository courseRepository;

    // Hiển thị chi tiết 1 khóa học
    @GetMapping("/{id}")
    public String getChiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học với id = " + id));

        if (course.getUrlGioiThieu() != null && !course.getUrlGioiThieu().isEmpty()) {
            course.setUrlGioiThieu(convertYoutubeUrl(course.getUrlGioiThieu()));
        }

        model.addAttribute("khoaHoc", course);
        return "views/KhoaHoc/XemChiTietKhoaHoc";
    }

    @GetMapping("/{id}/bai-giang")
    public String getDanhSachBaiGiang(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học với id = " + id));
        model.addAttribute("khoaHoc", khoaHoc);
        return "views/KhoaHoc/danhSachBaiGiang";
    }

    private String convertYoutubeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        String videoId = null;
        if (url.contains("youtube.com/watch?v=")) {
            videoId = url.substring(url.indexOf("v=") + 2);
            int ampersandPosition = videoId.indexOf('&');
            if (ampersandPosition != -1) {
                videoId = videoId.substring(0, ampersandPosition);
            }
        } else if (url.contains("youtu.be/")) {
            videoId = url.substring(url.indexOf("youtu.be/") + 9);
        }

        if (videoId != null) {
            return "https://www.youtube.com/embed/" + videoId;
        }
        // Return original URL if it's not a standard YouTube link
        return url;
    }
}
