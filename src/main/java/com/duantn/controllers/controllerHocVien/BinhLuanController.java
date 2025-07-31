package com.duantn.controllers.controllerHocVien;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.BinhLuan;
import com.duantn.entities.TaiKhoan;
import com.duantn.services.BaiGiangService;
import com.duantn.services.BinhLuanService;
import com.duantn.services.CustomUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/bai-giang/{baiGiangId}/binh-luan")
public class BinhLuanController {

    @Autowired
    private BinhLuanService binhLuanService;

    @Autowired
    private BaiGiangService baiGiangService;

    private TaiKhoan getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }
        return ((CustomUserDetails) auth.getPrincipal()).getTaiKhoan();
    }

    @GetMapping
    public String viewBinhLuan(@PathVariable("baiGiangId") Integer baiGiangId, Model model) {
        BaiGiang baiGiang = baiGiangService.findBaiGiangById(baiGiangId);

        if (baiGiang == null) {
            return "redirect:/khoahoc?error=notfound";
        }

        List<BinhLuan> rootComments = binhLuanService.getCommentsByBaiGiangId(baiGiangId);
        List<BinhLuan> allComments = binhLuanService.getAllCommentsByBaiGiangId(baiGiangId);

        Map<Integer, List<BinhLuan>> childrenMap = allComments.stream()
                .filter(c -> c.getParent() != null)
                .collect(Collectors.groupingBy(c -> c.getParent().getBinhluanId()));

        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("baiGiang", baiGiang);
        model.addAttribute("rootComments", rootComments);
        model.addAttribute("childrenMap", childrenMap);
        model.addAttribute("loggedInEmail", loggedInEmail);

        return "views/gdienHocVien/xem-khoa-hoc"; // Hoặc đường dẫn tới view thích hợp
    }

    // Lấy danh sách bình luận con (AJAX)
    @GetMapping("/replies/{parentId}")
    @ResponseBody
    public List<BinhLuan> getReplies(@PathVariable("parentId") Integer parentId) {
        return binhLuanService.getRepliesByParentCommentId(parentId);
    }

    // Thêm bình luận mới
    @PostMapping("/add")
    public String addComment(@PathVariable("baiGiangId") Integer baiGiangId,
            @RequestParam("noiDung") String noiDung,
            RedirectAttributes redirectAttributes) {
        TaiKhoan user = getCurrentUser();
        if (user == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn cần đăng nhập để bình luận.");
            return "redirect:/khoa-hoc/bai-giang/" + baiGiangId;
        }

        binhLuanService.saveComment(baiGiangId, noiDung, user.getTaikhoanId());
        redirectAttributes.addFlashAttribute("successMessage", "Bình luận của bạn đã được đăng.");
        return "redirect:/khoa-hoc/bai-giang/" + baiGiangId;
    }

    // Trả lời bình luận
    @PostMapping("/reply/{parentId}")
    @ResponseBody
    public ResponseEntity<?> replyComment(@PathVariable("baiGiangId") Integer baiGiangId,
            @PathVariable("parentId") Integer parentId,
            @RequestParam("noiDung") String noiDung) {
        TaiKhoan user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn cần đăng nhập để trả lời bình luận.");
        }

        BinhLuan reply = binhLuanService.replyToComment(baiGiangId, parentId, noiDung, user.getTaikhoanId());

        Map<String, Object> response = new HashMap<>();
        response.put("noiDung", reply.getNoiDung());
        response.put("taikhoanName", reply.getTaikhoan().getName());
        response.put("ngayBinhLuan", reply.getNgayBinhLuan().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        return ResponseEntity.ok(response);
    }

    // ✅ API xoá bình luận (AJAX)
    @DeleteMapping("/delete/{binhluanId}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable("baiGiangId") Integer baiGiangId,
            @PathVariable("binhluanId") Integer binhluanId) {
        TaiKhoan user = getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Bạn cần đăng nhập để xóa bình luận.");
        }

        boolean deleted = binhLuanService.deleteComment(binhluanId, user.getTaikhoanId());
        if (deleted) {
            return ResponseEntity.ok().body("Bình luận đã được xóa.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Không thể xóa bình luận này.");
        }
    }
}