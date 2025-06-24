package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.duantn.entities.Chuong;
import com.duantn.entities.Course;
import com.duantn.repositories.ChuongRepository;
import com.duantn.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ListKhoaHocController {

    private final CourseRepository courseRepository;
    private final ChuongRepository chuongRepository;

    // Hiển thị danh sách tất cả khóa học
    @GetMapping("/listKhoaHoc")
    public String danhSachKhoaHoc(Model model) {
        List<Course> list = courseRepository.findAll();
        model.addAttribute("courses", list);
        return "views/KhoaHoc/danhSachKhoaHoc";
    }

    // Chi tiết khóa học + danh sách chương
    @GetMapping("/detail/{id}")
    public String xemChiTiet(@PathVariable("id") Integer id, Model model) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học ID = " + id));

        List<Chuong> chuongList = chuongRepository.findByCourses_CourseId(id);
        if (chuongList.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy chương nào cho khóa học ID = " + id);
        }


        model.addAttribute("course", course);
        model.addAttribute("chuongList", chuongList);

        return "views/KhoaHoc/xemChiTietKhoaHoc";
    }
}
