package com.duantn.controllers.controllerAdmin;


import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.RoleRepository;
import com.duantn.repositories.TaiKhoanRepository;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/qlynhanvien")
public class QuanLyNhanVienController {
    @Autowired
    private TaiKhoanRepository taiKhoanRepo;

    @Autowired
    private RoleRepository roleRepo;

    @GetMapping
    public String hienThiDanhSach(Model model) {
        List<TaiKhoan> nhanviens = taiKhoanRepo.findByRoleRoleId(2);
        model.addAttribute("nhanviens", nhanviens);
        model.addAttribute("nhanvien", new TaiKhoan());
        return "views/gdienQuanLy/qLyNhanVien";
    }

    @PostMapping("/add")
    public String them(@ModelAttribute TaiKhoan nhanvien,
                       @RequestParam("avatarFile") MultipartFile file) {
        return luuOrCapNhat(nhanvien, file);
    }

    @GetMapping("/edit/{id}")
    public String chinhSua(@PathVariable Integer id, Model model) {
        TaiKhoan nv = taiKhoanRepo.findById(id).orElseThrow();
        model.addAttribute("nhanvien", nv);
        model.addAttribute("nhanviens", taiKhoanRepo.findByRoleRoleId(2));
        return "views/gdienQuanLy/qLyNhanVien";
    }

    @PostMapping("/edit")
    public String capNhat(@ModelAttribute TaiKhoan nhanvien,
                          @RequestParam("avatarFile") MultipartFile file) {
        return luuOrCapNhat(nhanvien, file);
    }

    private String luuOrCapNhat(TaiKhoan nv, MultipartFile file) {
        try {
            if (!file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get("src/main/resources/static/uploads");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath);
                nv.setAvatar("/uploads/" + fileName);
            }
            nv.setRole(roleRepo.findById(2).orElseThrow());
            taiKhoanRepo.save(nv);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/admin/qlynhanvien";
    }

    @PostMapping("/delete/{id}")
    public String xoa(@PathVariable Integer id) {
        taiKhoanRepo.deleteById(id);
        return "redirect:/admin/qlynhanvien";
    }
}
