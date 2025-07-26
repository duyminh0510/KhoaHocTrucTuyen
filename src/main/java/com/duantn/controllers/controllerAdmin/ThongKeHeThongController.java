package com.duantn.controllers.controllerAdmin;

import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.duantn.entities.DanhMuc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ThongKeHeThongController {

    private final TaiKhoanRepository taiKhoanRepo;
    private final KhoaHocRepository khoaHocRepo;
    private final RoleRepository roleRepo;

    @ManyToOne
    private DanhMuc danhMuc;

    @ManyToOne
    private TaiKhoan giangVien;

    // ✅ Trang thống kê hệ thống
    @GetMapping("/quanly-thong-ke-he-thong")
    public String showStatisticsPage(Model model) {
        List<TaiKhoan> hocViens = taiKhoanRepo.findByRoleRoleId(3); // ROLE_HOCVIEN
        List<TaiKhoan> giangViens = taiKhoanRepo.findByRoleRoleId(2); // ROLE_GIANGVIEN
        List<KhoaHoc> khoaHocs = khoaHocRepo.findByTrangThai(TrangThaiKhoaHoc.PUBLISHED);

        model.addAttribute("hocViens", hocViens);
        model.addAttribute("giangViens", giangViens);
        model.addAttribute("khoaHocs", khoaHocs);

        return "views/gdienQuanLy/quanly-thong-ke-he-thong";
    }

    // ✅ Danh sách học viên
    @GetMapping("/quanly-hocvien")
    public String danhSachHocVien(Model model) {
        Role hocVienRole = roleRepo.findByName("ROLE_HOCVIEN")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role học viên"));
        List<TaiKhoan> hocVienList = taiKhoanRepo.findByRole(hocVienRole);

        model.addAttribute("hocVienList", hocVienList);
        return "views/gdienQuanLy/danhsachhocvien";
    }

    // ✅ Form sửa học viên
    @GetMapping("/quanly-hocvien/edit/{id}")
    public String editHocVien(@PathVariable("id") Integer id, Model model) {
        TaiKhoan taiKhoan = taiKhoanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));
        model.addAttribute("taiKhoan", taiKhoan);
        return "views/gdienQuanLy/formhocvien";
    }

    // ✅ Lưu học viên
    @PostMapping("/quanly-hocvien/save")
    public String saveHocVien(@ModelAttribute("taiKhoan") @Valid TaiKhoan form,
            BindingResult bindingResult,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            RedirectAttributes redirectAttributes) {

        if (form.getTaikhoanId() == null) {
            redirectAttributes.addFlashAttribute("error", "Không thể thêm học viên mới.");
            return "redirect:/admin/quanly-hocvien";
        }

        if (bindingResult.hasErrors()) {
            return "views/gdienQuanLy/formhocvien";
        }

        TaiKhoan existing = taiKhoanRepo.findById(form.getTaikhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        existing.setName(form.getName());
        existing.setEmail(form.getEmail());
        existing.setPhone(form.getPhone());
        existing.setStatus(form.isStatus());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                existing.setAvatar(saveAvatarFile(avatarFile));
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu avatar!");
                return "redirect:/admin/quanly-hocvien";
            }
        }

        taiKhoanRepo.save(existing);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/admin/quanly-hocvien";
    }

    // ✅ Mở/Khóa học viên
    @PostMapping("/quanly-hocvien/toggle-status/{id}")
    public String toggleStatus(@PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {
        TaiKhoan hocVien = taiKhoanRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy học viên"));

        hocVien.setStatus(!hocVien.isStatus());
        taiKhoanRepo.save(hocVien);

        redirectAttributes.addFlashAttribute("success",
                hocVien.isStatus() ? "Tài khoản đã được mở khóa!" : "Tài khoản đã bị khóa!");

        return "redirect:/admin/quanly-hocvien";
    }

    // 👤 Hàm mô phỏng lưu avatar
    private String saveAvatarFile(MultipartFile file) throws IOException {
        return file.getOriginalFilename(); // Bạn nên thay bằng cách lưu file thực sự
    }
}
