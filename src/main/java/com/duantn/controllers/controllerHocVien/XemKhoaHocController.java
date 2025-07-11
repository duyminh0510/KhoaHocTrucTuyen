package com.duantn.controllers.controllerHocVien;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.Chuong;
import com.duantn.entities.KhoaHoc;
import com.duantn.services.BaiGiangService;
import com.duantn.services.ChuongService;
import com.duantn.services.KhoaHocService;

@Controller
public class XemKhoaHocController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private BaiGiangService baiGiangService;

    @RequestMapping("/khoa-hoc")
    public String xemkhoahoc(Model model) {
        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @RequestMapping("/khoa-hoc/slug/{slug}")
    public String hocbaicungto(@PathVariable("slug") String slug, Model model) {

        KhoaHoc khoaHoc = khoaHocService.getKhoaHocBySlug(slug);
        if (khoaHoc == null) {
            return "redirect:/khoaHoc?error=notfound";
        }

        Integer id = khoaHoc.getKhoahocId();
        List<Chuong> chuongs = chuongService.findByKhoaHocId(id);

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

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("chuongDangMoId", chuongDangMoId);
        if (baiGiangDauTien != null) {
            model.addAttribute("baiGiang", baiGiangDauTien);
            model.addAttribute("baiGiangDangHocId", baiGiangDauTien.getBaiGiangId());

            switch (baiGiangDauTien.getLoaiBaiGiang()) {
                case VIDEO -> model.addAttribute("video", baiGiangDauTien.getVideoBaiGiang());
                case TAILIEU -> model.addAttribute("baiViet", baiGiangDauTien.getBaiViet());
                case TRACNGHIEM -> model.addAttribute("baiTracNghiem", baiGiangDauTien.getTracNghiem());
            }
        }

        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @RequestMapping("/khoa-hoc/bai-giang/{id}")
    public String xemBaiGiang(@PathVariable("id") Integer baiGiangId, Model model) {
        BaiGiang baiGiang = baiGiangService.findBaiGiangById(baiGiangId);
        if (baiGiang == null) {
            return "redirect:/khoa-hoc?error=notfound";
        }

        Chuong chuong = baiGiang.getChuong();
        if (chuong == null || chuong.getKhoahoc() == null) {
            return "redirect:/khoa-hoc?error=nodata";
        }

        KhoaHoc khoaHoc = chuong.getKhoahoc();
        List<Chuong> chuongs = chuongService.findByKhoaHocId(khoaHoc.getKhoahocId());

        model.addAttribute("khoaHoc", khoaHoc);
        model.addAttribute("chuongs", chuongs);
        model.addAttribute("baiGiang", baiGiang);
        model.addAttribute("baiGiangDangHocId", baiGiangId);
        model.addAttribute("chuongDangMoId", baiGiang.getChuong().getChuongId());

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

        return "views/gdienHocVien/xem-khoa-hoc";
    }

}
