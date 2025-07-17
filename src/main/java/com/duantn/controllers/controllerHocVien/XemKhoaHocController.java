package com.duantn.controllers.controllerHocVien;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.duantn.entities.BaiGiang;
import com.duantn.entities.Chuong;
import com.duantn.entities.DangHoc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.entities.TienDoHoc;
import com.duantn.enums.LoaiBaiGiang;
import com.duantn.services.BaiGiangService;
import com.duantn.services.ChuongService;
import com.duantn.services.CustomUserDetails;
import com.duantn.services.DangHocService;
import com.duantn.services.KhoaHocService;
import com.duantn.services.TienDoHocService;

@Controller
public class XemKhoaHocController {

    @Autowired
    private KhoaHocService khoaHocService;

    @Autowired
    private ChuongService chuongService;

    @Autowired
    private BaiGiangService baiGiangService;

    @Autowired
    private TienDoHocService tienDoHocService;

    @Autowired
    private DangHocService dangHocService;

    @RequestMapping("/khoa-hoc")
    public String xemkhoahoc(Model model) {
        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @RequestMapping("/khoa-hoc/slug/{slug}")
    public String hocbaicungto(@PathVariable("slug") String slug, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan taiKhoan = userDetails.getTaiKhoan();
        model.addAttribute("taiKhoanId", taiKhoan.getTaikhoanId());

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

        DangHoc dangHoc = dangHocService.findByTaiKhoanIdAndKhoaHocId(taiKhoan.getTaikhoanId(), khoaHoc.getKhoahocId());

        if (dangHoc != null) {
            List<TienDoHoc> dsTienDo = tienDoHocService.findByDangHocId(dangHoc.getDanghocId());

            if (dsTienDo.isEmpty()) {
                tienDoHocService.taoTienDoChoDangHoc(dangHoc);
                dsTienDo = tienDoHocService.findByDangHocId(dangHoc.getDanghocId());
            }

            Map<Integer, Boolean> baiGiangDaHoanThanhMap = dsTienDo.stream()
                    .collect(Collectors.toMap(td -> td.getBaiGiang().getBaiGiangId(), TienDoHoc::isTrangthai));

            int tongSoBai = chuongs.stream()
                    .flatMap(ch -> ch.getBaiGiangs().stream())
                    .collect(Collectors.toList()).size();

            int soBaiHoanThanh = (int) baiGiangDaHoanThanhMap.values().stream().filter(v -> v).count();
            int phanTramHoanThanh = tongSoBai > 0 ? (int) ((double) soBaiHoanThanh / tongSoBai * 100) : 0;

            model.addAttribute("phanTramHoanThanh", phanTramHoanThanh);
            model.addAttribute("baiGiangDaHoanThanhMap", baiGiangDaHoanThanhMap);
        } else {
            // Khởi tạo biến mặc định để không lỗi Thymeleaf
            model.addAttribute("phanTramHoanThanh", 0);
            model.addAttribute("baiGiangDaHoanThanhMap", Map.of());
        }

        model.addAttribute("chuongDangMoId", chuongDangMoId);
        if (baiGiangDauTien != null) {
            model.addAttribute("baiGiang", baiGiangDauTien);
            model.addAttribute("baiGiangDangHocId", baiGiangDauTien.getBaiGiangId());

            switch (baiGiangDauTien.getLoaiBaiGiang()) {
                case VIDEO -> model.addAttribute("video", baiGiangDauTien.getVideoBaiGiang());
                case TAILIEU -> model.addAttribute("baiViet", baiGiangDauTien.getBaiViet());
                // case TRACNGHIEM -> model.addAttribute("baiTracNghiem",
                // baiGiangDauTien.getTracNghiem());
                case TRACNGHIEM -> {
                    model.addAttribute("baiTracNghiem", baiGiangDauTien.getTracNghiem());
                    int stt = tinhThuTuBaiTracNghiem(baiGiangDauTien.getBaiGiangId(), chuongs);
                    model.addAttribute("thuTuBaiTracNghiem", stt);
                    model.addAttribute("tongSoCauHoi", baiGiangDauTien.getTracNghiem().getCauHoiList().size());
                }
            }
        }

        return "views/gdienHocVien/xem-khoa-hoc";
    }

    @RequestMapping("/khoa-hoc/bai-giang/{id}")
    public String xemBaiGiang(@PathVariable("id") Integer baiGiangId, Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return "redirect:/auth/dangnhap";
        }

        TaiKhoan taiKhoan = userDetails.getTaiKhoan();
        model.addAttribute("taiKhoanId", taiKhoan.getTaikhoanId());

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
                int stt = tinhThuTuBaiTracNghiem(baiGiang.getBaiGiangId(), chuongs);
                model.addAttribute("thuTuBaiTracNghiem", stt);
                model.addAttribute("tongSoCauHoi", baiGiang.getTracNghiem().getCauHoiList().size());
                break;
        }

        return "views/gdienHocVien/xem-khoa-hoc";
    }

    private int tinhThuTuBaiTracNghiem(Integer baiGiangId, List<Chuong> chuongs) {
        int stt = 0;
        for (Chuong c : chuongs) {
            if (c.getBaiGiangs() == null)
                continue;
            for (BaiGiang bg : c.getBaiGiangs()) {
                if (bg.getLoaiBaiGiang() == LoaiBaiGiang.TRACNGHIEM) {
                    stt++;
                    if (bg.getBaiGiangId().equals(baiGiangId)) {
                        return stt;
                    }
                }
            }
        }
        return 0;
    }

}
