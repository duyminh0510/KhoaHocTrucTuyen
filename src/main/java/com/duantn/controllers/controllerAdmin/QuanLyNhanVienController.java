package com.duantn.controllers.controllerAdmin;

import com.duantn.entities.Role;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/quanly-nhanvien")
public class QuanLyNhanVienController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<TaiKhoan> nhanviens = taiKhoanRepo.findByRoleRoleId(2);
        model.addAttribute("nhanviens", nhanviens);
        model.addAttribute("nhanvien", new TaiKhoan()); // dùng để bind form thêm mới
        return "views/gdienQuanLy/danhsachnhanvien";
    }

    @PostMapping("/add")
    public String themNhanVien(@ModelAttribute TaiKhoan nhanvien,
            @RequestParam("avatarFile") MultipartFile file) {
        nhanvien.setTaikhoanId(null); // đảm bảo là thêm mới
        return luuHoacCapNhat(nhanvien, file);
    }

    @GetMapping("/edit/{id}")
    public String formChinhSua(@PathVariable Integer id, Model model) {
        TaiKhoan nhanvien = taiKhoanRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên ID: " + id));
        model.addAttribute("nhanvien", nhanvien);
        model.addAttribute("nhanviens", taiKhoanRepo.findByRoleRoleId(2));
        return "views/gdienQuanLy/danhsachnhanvien";
    }

    @PostMapping("/edit")
    public String capNhatNhanVien(@ModelAttribute TaiKhoan nhanvien,
            @RequestParam("avatarFile") MultipartFile file) {
        return luuHoacCapNhat(nhanvien, file);
    }

    @SuppressWarnings("null")
    private String luuHoacCapNhat(TaiKhoan nhanvien, MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                if (!file.getContentType().startsWith("image/")) {
                    throw new IllegalArgumentException("File không phải hình ảnh.");
                }
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR);
                if (!Files.exists(path)) {
                    Files.createDirectories(path);
                }
                Path filePath = path.resolve(fileName);
                file.transferTo(filePath.toFile());
                nhanvien.setAvatar("/uploads/" + fileName);
            }

            Role nhanVienRole = roleRepo.findById(2)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy role nhân viên"));
            nhanvien.setRole(nhanVienRole);

            if (nhanvien.getTaikhoanId() != null) {
                // Đang sửa -> giữ nguyên mật khẩu
                TaiKhoan existing = taiKhoanRepo.findById(nhanvien.getTaikhoanId())
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhân viên cần cập nhật"));
                nhanvien.setPassword(existing.getPassword());
            } else {
                // Đang thêm mới -> mã hóa mật khẩu
                nhanvien.setPassword(passwordEncoder.encode(nhanvien.getPassword()));
            }

            taiKhoanRepo.save(nhanvien);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/admin/quanly-nhanvien";
    }

    @PostMapping("/delete/{id}")
    public String xoaNhanVien(@PathVariable Integer id) {
        taiKhoanRepo.deleteById(id);
        return "redirect:/admin/quanly-nhanvien";
    }
}
