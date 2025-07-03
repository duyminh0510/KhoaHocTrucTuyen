package com.duantn.controllers.controllerKhoaHoc;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.enums.TrangThaiKhoaHoc;
import com.duantn.repositories.KhoaHocRepository;
import com.duantn.repositories.NguoiDungThichKhoaHocRepository;
import com.duantn.repositories.TaiKhoanRepository;
import com.duantn.services.ChuongService;
import com.duantn.services.KhoaHocService;

@Controller
public class ChiTietPheDuyetController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private NguoiDungThichKhoaHocRepository nguoiDungThichKhoaHocRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private KhoaHocRepository khoaHocRepository;


    @GetMapping("/duyetChiTiet/{id}")
    public String chiTietKhoaHoc(@PathVariable("id") Integer id, Model model) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocById(id);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        List<Chuong> chuongs = chuongService.findByKhoaHocId(id);

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        return "views/KhoaHoc/PheDuyetChiTietKhoaHoc";
    }

    @PostMapping("/phe-duyet/{id}")
    public String pheDuyet(@PathVariable("id") Integer id) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id).orElse(null);
        if (khoaHoc != null && khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
            khoaHoc.setTrangThai(TrangThaiKhoaHoc.PUBLISHED);
            khoaHocRepository.save(khoaHoc);
        }
        return "redirect:/duyetKhoaHoc/danh-sach";
    }

    @PostMapping("/tu-choi/{id}")
    public String tuChoi(@PathVariable("id") Integer id) {
        KhoaHoc khoaHoc = khoaHocRepository.findById(id).orElse(null);
        if (khoaHoc != null && khoaHoc.getTrangThai() == TrangThaiKhoaHoc.PENDING_APPROVAL) {
            khoaHoc.setTrangThai(TrangThaiKhoaHoc.REJECTED);
            khoaHocRepository.save(khoaHoc);
        }
        return "redirect:/duyetKhoaHoc/danh-sach";
    }
}
