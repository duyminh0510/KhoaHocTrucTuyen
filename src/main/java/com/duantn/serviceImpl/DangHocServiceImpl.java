package com.duantn.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.duantn.entities.DangHoc;
import com.duantn.entities.ChungChi;
import com.duantn.repositories.DangHocRepository;
import com.duantn.repositories.ChungChiRepository;
import com.duantn.services.DangHocService;
import com.duantn.services.CertificatePdfGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class DangHocServiceImpl implements DangHocService {

    @Autowired
    private DangHocRepository dangHocRepository;

    @Autowired
    private ChungChiRepository chungChiRepository;

    @Autowired
    private CertificatePdfGenerator certificatePdfGenerator;

    @Override
    public long demSoLuongDangKy(Integer khoaHocId) {
        return dangHocRepository.countByKhoahoc_KhoahocId(khoaHocId);
    }

    @Override
    public DangHoc findByTaiKhoanIdAndKhoaHocId(Integer taiKhoanId, Integer khoaHocId) {
        return dangHocRepository.findByTaikhoan_TaikhoanIdAndKhoahoc_KhoahocId(taiKhoanId, khoaHocId);
    }

    @Override
    public void capNhatTrangThaiVaTaoChungChi(DangHoc dangHoc) {
        if (dangHoc == null || dangHoc.isDaCap_ChungChi()) return;
        try {
            // Tạo mã chứng chỉ duy nhất
            String maChungChi = UUID.randomUUID().toString();
            // Tạo file chứng chỉ PDF
            String filePath = certificatePdfGenerator.generate(
                dangHoc.getTaikhoan().getName(),
                dangHoc.getKhoahoc().getTenKhoaHoc(),
                LocalDate.now(),
                maChungChi
            );
            // Tạo entity ChungChi
            ChungChi chungChi = ChungChi.builder()
                .maChungChi(maChungChi)
                .ngayCap(LocalDateTime.now())
                .duongDanFile(filePath)
                .tenKhoaHoc(dangHoc.getKhoahoc().getTenKhoaHoc())
                .maHocVien(dangHoc.getTaikhoan().getTaikhoanId())
                .tenHocVien(dangHoc.getTaikhoan().getName())
                .danghoc(dangHoc)
                .build();
            chungChiRepository.save(chungChi);
            // Đánh dấu đã cấp chứng chỉ
            dangHoc.setDaCap_ChungChi(true);
            dangHoc.setNgayHoanThanh(LocalDateTime.now());
            dangHocRepository.save(dangHoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isEnrolled(Integer taiKhoanId, Integer khoaHocId) {
        DangHoc dangHoc = findByTaiKhoanIdAndKhoaHocId(taiKhoanId, khoaHocId);
        return dangHoc != null;
    }
    

}
