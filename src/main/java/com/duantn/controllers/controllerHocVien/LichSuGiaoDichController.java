// package com.duantn.controllers.controllerHocVien;

// import com.duantn.entities.GiaoDichKhoaHoc;
// import com.duantn.entities.TaiKhoan;
// import com.duantn.repositories.TaiKhoanRepository;
// import com.duantn.services.GiaoDichService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

// import java.util.Collections;
// import java.util.List;

// @Controller
// @RequestMapping("/hoc-vien")
// public class LichSuGiaoDichController {

//     @Autowired
//     private GiaoDichService giaoDichService;

//     @Autowired
//     private TaiKhoanRepository taiKhoanRepository;

//     @GetMapping("/lich-su-giao-dich")
//     public String showTransactionHistory(Model model) {
//         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//         String email = authentication.getName();
//         TaiKhoan currentUser = taiKhoanRepository.findByEmail(email)
//                 .orElse(null);

//         if (currentUser != null) {
//             List<GiaoDichKhoaHoc> transactions = giaoDichService.findByTaiKhoanId(currentUser.getId());
//             model.addAttribute("danhSachGiaoDich", transactions);
//         } else {
//             model.addAttribute("danhSachGiaoDich", Collections.emptyList());
//         }

//         return "views/gdienHocVien/lich-su-giao-dich";
//     }
// } 