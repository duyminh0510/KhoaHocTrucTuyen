package com.duantn.controllers.controllerHocVien;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.TaiKhoanRepository;

@Controller
public class ThongTinCaNhanController {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    private static final String UPLOAD_DIR = "D:\\datn\\3\\duantn\\src\\main\\resources\\static\\photos\\";

    @GetMapping("/tai-khoan")
    public String hienThiTaiKhoan(Model model, Authentication authentication) {
        String email = authentication.getName();
        taiKhoanRepository.findByEmail(email).ifPresent(tk -> model.addAttribute("taiKhoan", tk));
        return "views/gdienHocVien/tai-khoan";
    }

    @PostMapping("/tai-khoan/cap-nhat")
    public String capNhatTaiKhoan(@RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("avatar") MultipartFile avatarFile,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        String email = authentication.getName();
        TaiKhoan tk = taiKhoanRepository.findByEmail(email).orElse(null);

        if (tk == null) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy tài khoản.");
            return "redirect:/tai-khoan";
        }

        tk.setName(name);
        tk.setPhone(phone);

        // Xử lý ảnh nếu có upload
        if (!avatarFile.isEmpty()) {
            try {
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists())
                    uploadDir.mkdirs();

                String fileName = UUID.randomUUID() + "_" + avatarFile.getOriginalFilename();
                File file = new File(uploadDir, fileName);
                avatarFile.transferTo(file);

                // Lưu đường dẫn tương đối (sử dụng cho th:src)
                tk.setAvatar("/photos/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Lỗi khi tải ảnh.");
                return "redirect:/tai-khoan";
            }
        }

        taiKhoanRepository.save(tk);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công.");
        return "redirect:/tai-khoan";
    }
}
