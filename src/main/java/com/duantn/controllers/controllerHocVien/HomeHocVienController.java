package com.duantn.controllers.controllerHocVien;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.KhoaHocService;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/hoc-vien")
public class HomeHocVienController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private KhoaHocRepository khoaHocRepository;

    @GetMapping("")
    public String showHomeHocVien(Model model, Authentication authentication) {
        List<KhoaHoc> khoaHocList = khoaHocService.getTatCaKhoaHoc();
        model.addAttribute("khoaHocList", khoaHocList);

        if (authentication != null && authentication.isAuthenticated()) {
            taiKhoanRepository.findByEmail(authentication.getName()).ifPresent(taiKhoan -> {
                Set<Integer> likedCourseIds = nguoiDungThichKhoaHocRepository
                        .findByTaiKhoan_TaikhoanId(taiKhoan.getTaikhoanId()).stream()
                        .map(like -> like.getKhoaHoc().getKhoahocId()).collect(Collectors.toSet());
                model.addAttribute("likedCourseIds", likedCourseIds);
            });
        }
        if (!model.containsAttribute("likedCourseIds")) {
            model.addAttribute("likedCourseIds", Collections.emptySet());
        }

        return "views/gdienHocVien/home";
    }

    @GetMapping("/khoa-hoc-yeu-thich")
    public String showKhoaHocYeuThich(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        Integer currentUserId = taiKhoanRepository.findByEmail(authentication.getName())
                .map(taiKhoan -> taiKhoan.getTaikhoanId()).orElse(null);

        List<KhoaHoc> favoriteCourses = khoaHocService.findLikedCoursesByAccountId(currentUserId);
        model.addAttribute("favoriteCourses", favoriteCourses);
        return "views/gdienHocVien/khoa-hoc-yeu-thich";
    }

    @PostMapping("/khoa-hoc/{id}/like")
    @ResponseBody
    public ResponseEntity<?> likeCourse(@PathVariable Integer id, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Vui lòng đăng nhập.");
        }

        return taiKhoanRepository.findByEmail(authentication.getName()).map(taiKhoan -> {
            boolean isLiked = khoaHocService.toggleLike(id, taiKhoan.getTaikhoanId());
            KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);

            if (khoaHoc != null) {
                Map<String, Object> response = Map.of("newLikeCount",
                        khoaHoc.getLuotThich() != null ? khoaHoc.getLuotThich() : 0, "isLiked",
                        isLiked);
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.notFound().build();
        }).orElse(ResponseEntity.status(404).body("Không tìm thấy tài khoản."));
    }

    @PostMapping("/share-course")
    public String shareCourseByEmail(@RequestParam("courseId") Integer courseId,
            @RequestParam("recipientEmail") String recipientEmail, Authentication authentication,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("share_error", "Vui lòng đăng nhập để chia sẻ.");
            return "redirect:/login";
        }

        try {
            TaiKhoan sender = taiKhoanRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy người gửi."));

            KhoaHoc course = khoaHocRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy khóa học."));

            sendShareEmail(sender.getName(), recipientEmail, course, request);

            redirectAttributes.addFlashAttribute("share_success",
                    "Khóa học đã được chia sẻ thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("share_error",
                    "Gửi email thất bại: " + e.getMessage());
        }

        return "redirect:/";
    }

    private void sendShareEmail(String senderName, String recipientEmail, KhoaHoc course,
            HttpServletRequest request) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject(senderName + " đã chia sẻ một khóa học thú vị với bạn!");
        helper.setFrom("globaledu237@gmail.com", "GlobalEdu");

        String courseUrl = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + "/hoc-vien/khoa-hoc/" + course.getKhoahocId();

        String htmlContent = String.format(
                """
                        <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                            <h2 style="color: #0056b3;">Xin chào,</h2>
                            <p>Người bạn <strong>%s</strong> của bạn nghĩ rằng bạn sẽ thích khóa học này trên GlobalEdu:</p>

                            <div style="border: 1px solid #ddd; padding: 15px; border-radius: 8px; margin: 20px 0;">
                                <h3 style="margin-top: 0;">%s</h3>
                                <p>%s</p>
                                <a href="%s" style="display: inline-block; background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Xem chi tiết khóa học</a>
                            </div>

                            <p>Hãy khám phá và nâng cao kiến thức của bạn ngay hôm nay!</p>
                            <br>
                            <p>Trân trọng,<br><strong>Đội ngũ GlobalEdu</strong></p>
                        </div>
                        """,
                senderName, course.getTenKhoaHoc(), course.getMoTa(), courseUrl);

        helper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }
}
