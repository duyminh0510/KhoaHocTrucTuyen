package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.Course;
import com.duantn.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ListKhoaHocController {

    private final CourseRepository courseRepository;

    // Hiển thị danh sách tất cả khóa học
    @GetMapping("/listKhoaHoc")
    public String danhSachKhoaHoc(Model model) {
        List<Course> list = courseRepository.findAll();
        model.addAttribute("courses", list);
        return "views/KhoaHoc/danhSachKhoaHoc"; // Trả về HTML file: course-list.html
    }

    @GetMapping("/detail/{id}")
    public String xemChiTiet(@PathVariable("id") Integer id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học ID = " + id));
        model.addAttribute("course", course);
        return "views/KhoaHoc/xemChiTietKhoaHoc"; // File HTML tương ứng
    }

}
