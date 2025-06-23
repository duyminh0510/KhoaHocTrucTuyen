package com.duantn.controllers.controllerKhoaHoc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.Course;
import com.duantn.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/khoa-hoc")
@RequiredArgsConstructor
public class CourseController {

    private final CourseRepository courseRepository;

    // Hiển thị chi tiết 1 khóa học
    @GetMapping("/{id}")
    public String getChiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học với id = " + id));

        model.addAttribute("course", course);
        return "views/KhoaHoc"; // Tên file HTML trong templates
    }
}
