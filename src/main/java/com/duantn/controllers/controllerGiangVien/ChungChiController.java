package com.duantn.controllers.controllerGiangVien;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.duantn.entities.ChungChi;
import com.duantn.entities.DangHoc;
import com.duantn.entities.KhoaHoc;
import com.duantn.entities.TaiKhoan;
import com.duantn.repositories.ChungChiRepository;
import com.duantn.repositories.DangHocRepository;
import com.duantn.services.CertificatePdfGenerator;

@RestController
@RequestMapping("/api/chung-chi")
class ChungChiController {

    @Autowired
    private DangHocRepository dangHocRepo;

    @Autowired
    private ChungChiRepository chungChiRepo;

    @Autowired
    private CertificatePdfGenerator pdfGenerator;

    @PostMapping("/cap-nhat/{danghocId}")
    public String capNhatTrangThaiVaTaoChungChi(@PathVariable Integer danghocId) throws Exception {
        DangHoc dh = dangHocRepo.findById(danghocId).orElseThrow();
        if (dh.isTrangthai()) return "Đã hoàn thành";

        dh.setTrangthai(true);
        dangHocRepo.save(dh);

        TaiKhoan hocVien = dh.getTaikhoan();
        KhoaHoc khoaHoc = dh.getKhoahoc();

        String fileName = "certificate_" + hocVien.getTaikhoanId();
        String filePath = pdfGenerator.generate(hocVien.getName(), khoaHoc.getTenKhoaHoc(), LocalDate.now(), fileName);

        ChungChi cc = new ChungChi();
        cc.setDanghoc(dh);
        cc.setTenHocVien(hocVien.getName());
        cc.setTenKhoaHoc(khoaHoc.getTenKhoaHoc());
        cc.setNgayCap(LocalDateTime.now());
        cc.setMaChungChi(UUID.randomUUID().toString());
        cc.setDuongDanFile(filePath);
        chungChiRepo.save(cc);

        return "Chứng chỉ đã tạo tại: " + filePath;
    }
}

