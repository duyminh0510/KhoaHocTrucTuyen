package com.duantn.controllers.controllerAdmin;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.repositories.ThuNhapNenTangRepository;

@Controller
@RequestMapping("/admin")
public class DashboardQuanLyController {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @Autowired
    private ThuNhapNenTangRepository thuNhapNenTangRepository;

    @GetMapping({"", "/"})
    public String home(Model model) {
        Role giangVienRole = roleRepository.findByName("ROLE_GIANGVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền giảng viên"));
        Role hocVienRole = roleRepository.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền học viên"));

        List<TaiKhoan> giangViens = taiKhoanRepository.findByRole(giangVienRole);
        List<TaiKhoan> hocViens = taiKhoanRepository.findByRole(hocVienRole);
        List<KhoaHoc> khoaHocDangMo = khoaHocRepository.findByTrangThai(TrangThaiKhoaHoc.PUBLISHED);

        // Gọi tổng thu nhập tháng này
        BigDecimal tongThuNhap = thuNhapNenTangRepository.getTongThuNhap();
        model.addAttribute("tongThuNhap", tongThuNhap);


        model.addAttribute("soGiangVien", giangViens.size());
        model.addAttribute("soHocVien", hocViens.size());
        model.addAttribute("soKhoaHoc", khoaHocDangMo.size());
        model.addAttribute("tongThuNhap", tongThuNhap);

        return "views/gdienQuanly/home";
    }
}
