package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.BaiGiang;
import com.duantn.entities.KhoaHoc;
import com.duantn.repositories.BaiGiangRepository;
import com.duantn.repositories.KhoaHocRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/baiGiang")
@RequiredArgsConstructor
public class DanhSachBaiGiangController {

    private final KhoaHocRepository khoaHocRepository;
    private final BaiGiangRepository baiGiangRepository;

    // Hiển thị danh sách bài giảng của một khóa học cụ thể
    @GetMapping("/{id}/danh-sach")
    public String getDanhSachBaiGiang(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Không tìm thấy khóa học với id = " + id));

        model.addAttribute("khoaHoc", khoaHoc);
        return "views/BaiGiang/danhSachBaiGiang";
    }

    // Hiển thị TẤT CẢ các bài giảng
    @GetMapping("/tat-ca")
    public String getAllBaiGiang(Model model) {
        List<BaiGiang> dsBaiGiang = baiGiangRepository.findAll();
        model.addAttribute("dsBaiGiang", dsBaiGiang);
        return "views/BaiGiang/danhsach-tatca-baigiang";
    }
}
