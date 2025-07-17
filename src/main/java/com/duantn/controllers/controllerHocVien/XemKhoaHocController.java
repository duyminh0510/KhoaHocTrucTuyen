package com.duantn.controllers.controllerHocVien;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.BinhLuan;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.BaiGiangService;
import com.duantn.services.BinhLuanService;
import com.duantn.services.ChuongService;
import com.duantn.services.KhoaHocService;

@Controller
@RequestMapping("/khoahoc")
public class XemKhoaHocController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private BaiGiangService baiGiangService;

    @Autowired
    private BinhLuanService binhLuanService;

    @GetMapping
    public String xemKhoaHocTrangChinh(Model model) {
        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @GetMapping("/slug/{slug}")
    public String xemKhoaHocTheoSlug(@PathVariable("slug") String slug, Model model) {
        KhoaHoc khoaHoc = khoaHocService.getKhoaHocBySlug(slug);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        Integer khoaHocId = khoaHoc.getKhoahocId();
        List<Chuong> chuongs = chuongService.findByKhoaHocId(khoaHocId);

        BaiGiang baiGiangDauTien = null;
        Integer chuongDangMoId = null;

        if (chuongs != null && !chuongs.isEmpty()) {
            for (Chuong chuong : chuongs) {
                if (chuong.getBaiGiangs() != null && !chuong.getBaiGiangs().isEmpty()) {
                    baiGiangDauTien = chuong.getBaiGiangs().get(0);
                    chuongDangMoId = chuong.getChuongId();
                    break;
                }
            }
        }

        if (baiGiangDauTien != null) {
            addBaiGiangToModel(baiGiangDauTien, model);
            addBinhLuanToModel(baiGiangDauTien.getBaiGiangId(), model);
        }

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("chuongDangMoId", chuongDangMoId);

        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @GetMapping("/baigiang/{id}")
    public String xemBaiGiang(@PathVariable("id") Integer baiGiangId, Model model) {
        BaiGiang baiGiang = baiGiangService.findBaiGiangById(baiGiangId);
        if (baiGiang == null) {
            return "redirect:/khoa-hoc?error=notfound";
        }

        Chuong chuong = baiGiang.getChuong();
        if (chuong == null || chuong.getKhoahoc() == null) {
            return "redirect:/khoahoc?error=nodata";
        }

        KhoaHoc khoaHoc = chuong.getKhoahoc();
        List<Chuong> chuongs = chuongService.findByKhoaHocId(khoaHoc.getKhoahocId());

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("chuongDangMoId", chuong.getChuongId());

        addBaiGiangToModel(baiGiang, model);
        addBinhLuanToModel(baiGiangId, model);

        return "views/gdienHocVien/xem-khoa-hoc";
    }

    private void addBaiGiangToModel(BaiGiang baiGiang, Model model) {
        model.addAttribute("baiGiang", baiGiang);
        model.addAttribute("baiGiangDangHocId", baiGiang.getBaiGiangId());

        switch (baiGiang.getLoaiBaiGiang()) {
            case VIDEO:
                model.addAttribute("video", baiGiang.getVideoBaiGiang());
                break;
            case TAILIEU:
                model.addAttribute("baiViet", baiGiang.getBaiViet());
                break;
            case TRACNGHIEM:
                model.addAttribute("baiTracNghiem", baiGiang.getTracNghiem());
                break;
        }
    }

    private void addBinhLuanToModel(Integer baiGiangId, Model model) {
        List<BinhLuan> rootComments = binhLuanService.getCommentsByBaiGiangId(baiGiangId);
        List<BinhLuan> allComments = binhLuanService.getAllCommentsByBaiGiangId(baiGiangId);

        Map<Integer, List<BinhLuan>> childrenMap = allComments.stream()
                .filter(c -> c.getParent() != null)
                .collect(Collectors.groupingBy(c -> c.getParent().getBinhluanId()));

        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("rootComments", rootComments);
        model.addAttribute("childrenMap", childrenMap);
        model.addAttribute("loggedInEmail", loggedInEmail);
    }
}
