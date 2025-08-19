package com.duantn.controllers.controllerGiangVien;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.duantn.entities.GiangVien;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.RutTienGiangVien;
import com.duantn.services.AuthService;
import com.duantn.services.GiangVienService;
import com.duantn.services.ViGiangVienService;
import com.duantn.services.TokenService;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/giangvien/vi-giang-vien")
public class ViGiangVienController {
    @Autowired
    private ViGiangVienService viGiangVienService;

    @Autowired
    private GiangVienService giangVienService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @GetMapping
    public String hienThiVi(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(auth);

        if (giangVien == null) {
            return "redirect:/auth/dangnhap";
        }

        model.addAttribute("soDu", viGiangVienService.tinhSoDu(giangVien));
        model.addAttribute("lichSuThuNhap", viGiangVienService.getLichSuThuNhap(giangVien));
        model.addAttribute("lichSuRutTien", viGiangVienService.getLichSuRutTienThanhCong(giangVien));
        model.addAttribute("yeuCauDangXuLy", viGiangVienService.getYeuCauDangXuLy(giangVien));

        model.addAttribute("tongThuThang", viGiangVienService.getTongThuTrongThang(giangVien));
        model.addAttribute("soLanNhanThang", viGiangVienService.getSoLanNhanTrongThang(giangVien));

        return "views/gdienGiangVien/vi-giang-vien";
    }

    @PostMapping("/rut-tien")
    public String guiYeuCauRutTien(@RequestParam("soTienRut") BigDecimal soTienRut) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(auth);

        if (giangVien == null) {
            return "redirect:/auth/dangnhap";
        }

        if (viGiangVienService.guiYeuCauRutTien(giangVien, soTienRut)) {
            return "redirect:/giangvien/vi-giang-vien?success";
        }
        return "redirect:/giangvien/vi-giang-vien?error";
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getThongTin(@PathVariable("id") Integer id) {
        GiangVien gv = giangVienService.getById(id);
        if (gv != null) {
            return ResponseEntity.ok(gv); // Spring boot sẽ tự chuyển thành JSON
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cap-nhat-ngan-hang")
    public void capNhatThongTinNganHang(
            @RequestParam("giangVienId") Integer giangVienId,
            @RequestParam("soTaiKhoan") String soTaiKhoan,
            @RequestParam("tenNganHang") String tenNganHang,
            @RequestParam("otp") String otp,
            HttpServletResponse response) {

        boolean valid = tokenService.verifyToken(otp).isPresent();
        if (!valid) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            return;
        }

        boolean success = giangVienService.capNhatThongTinNganHang(giangVienId, soTaiKhoan, tenNganHang);
        if (success) {
            response.setStatus(HttpServletResponse.SC_OK); // 200
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
        }
    }

    // API lấy thông tin tài khoản/ngân hàng gần nhất
    @GetMapping("/thong-tin-ngan-hang")
    @ResponseBody
    public ResponseEntity<?> getThongTinNganHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(authentication);

        if (giangVien == null) {
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");
        }

        RutTienGiangVien last = viGiangVienService.getLastRutTien(giangVien);
        if (last != null && last.getSoTaiKhoan() != null && last.getTenNganHang() != null) {
            Map<String, String> result = new HashMap<>();
            result.put("soTaiKhoan", last.getSoTaiKhoan());
            result.put("tenNganHang", last.getTenNganHang());
            result.put("readonly", "true");
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.ok().build();
    }

    // API gửi OTP về email khi rút tiền
    @PostMapping("/gui-otp")
    @ResponseBody
    public ResponseEntity<?> guiOtpRutTien(@RequestParam String soTaiKhoan, @RequestParam String tenNganHang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(authentication);

        if (giangVien == null) {
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");
        }

        String email = giangVien.getEmail();
        String name = giangVien.getName();
        String subject = "Mã xác thực rút tiền";
        String contentPrefix = "Mã xác thực rút tiền cho tài khoản ngân hàng: " + soTaiKhoan + ", ngân hàng: "
                + tenNganHang;
        tokenService.generateAndSendToken(email, name, subject, contentPrefix);
        return ResponseEntity.ok().build();
    }

    // API xác thực OTP và tạo yêu cầu rút tiền
    @PostMapping("/xac-thuc-otp")
    @ResponseBody
    public ResponseEntity<?> xacThucOtpRutTien(@RequestParam String soTaiKhoan, @RequestParam String tenNganHang,
            @RequestParam BigDecimal soTienRut, @RequestParam String otp) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(authentication);

        if (giangVien == null) {
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");
        }

        boolean valid = tokenService.verifyToken(otp).isPresent();
        if (!valid) {
            return ResponseEntity.badRequest().body("Mã xác thực không đúng hoặc đã hết hạn!");
        }
        boolean success = viGiangVienService.guiYeuCauRutTienFull(giangVien, soTienRut, soTaiKhoan, tenNganHang);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Số tiền rút không hợp lệ hoặc vượt quá số dư!");
    }

    // ✅ API gửi OTP khi đổi STK (khác với rút tiền)
    @PostMapping("/gui-otp-doi-stk")
    @ResponseBody
    public ResponseEntity<?> guiOtpDoiSTK(@RequestParam String soTaiKhoan, @RequestParam String tenNganHang) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan giangVien = authService.getTaiKhoanFromAuth(authentication);

        if (giangVien == null) {
            return ResponseEntity.status(401).body("Bạn cần đăng nhập");
        }

        String email = giangVien.getEmail();
        String name = giangVien.getName();

        String subject = "Mã xác thực đổi tài khoản ngân hàng";
        String contentPrefix = "Mã xác thực để đổi tài khoản ngân hàng thành: " + soTaiKhoan + ", ngân hàng: "
                + tenNganHang;

        tokenService.generateAndSendToken(email, name, subject, contentPrefix);
        return ResponseEntity.ok().build();
    }
}