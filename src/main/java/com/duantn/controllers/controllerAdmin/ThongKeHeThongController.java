package com.duantn.controllers.controllerAdmin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ThongKeHeThongController {

    private final TaiKhoanRepository taiKhoanRepo;
    private final KhoaHocRepository khoaHocRepo;
    private final RoleRepository roleRepo;

    // Trang thống kê có phân trang
    @GetMapping("/quanly-thong-ke-he-thong")
    public String showStatisticsPage(@RequestParam(defaultValue = "0") int pageHocVien,
            @RequestParam(defaultValue = "0") int pageGiangVien,
            @RequestParam(defaultValue = "0") int pageKhoaHoc, Model model) {

        Role hocVienRole = roleRepo.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role học viên"));
        Role giangVienRole = roleRepo.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role giảng viên"));

        Pageable pageableHv = PageRequest.of(pageHocVien, 10);
        Pageable pageableGv = PageRequest.of(pageGiangVien, 10);
        Pageable pageableKh = PageRequest.of(pageKhoaHoc, 10);

        Page<TaiKhoan> hocViensPage = taiKhoanRepo.findByRole(hocVienRole, pageableHv);
        Page<TaiKhoan> giangViensPage = taiKhoanRepo.findByRole(giangVienRole, pageableGv);
        Page<KhoaHoc> khoaHocsPage = khoaHocRepo.findByTrangThai(TrangThaiKhoaHoc.PUBLISHED, pageableKh);

        // Nếu bạn vẫn muốn giữ danh sách nguyên bản (ko phân trang) để hiển thị thì
        // model vẫn giữ
        // Nhưng giờ mình sẽ trả về Page để view có thể phân trang
        model.addAttribute("pageHocViens", hocViensPage);
        model.addAttribute("pageGiangViens", giangViensPage);
        model.addAttribute("pageKhoaHocs", khoaHocsPage);

        return "views/gdienQuanLy/quanly-thong-ke-he-thong";
    }
}